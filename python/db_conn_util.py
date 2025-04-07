"""Modules providing a function tracking time and sql connection"""
import time
import mysql.connector
from company_address_api import geocode_address

# use host='algorise-mysql' for docker


def connect_to_mysql():
    """
    Establishes a connection to the MySQL database with a retry mechanism.

    This function attempts to connect to the MySQL database using predefined
    credentials and parameters. If the initial connection attempt fails,
    it retries with an exponential backoff strategy up to a maximum number of retries.

    Returns:
        connection: A MySQLConnection object if the connection is successful.

    Raises:
        ValueError: If the function fails to connect to the MySQL database
        after the specified number of retries.

    Note:
        The function is configured to connect to a database named 'GeoJobSearch'
        on the host 'algorise-mysql' using the root user. Modify these parameters
        as necessary to match your database configuration.
    """
    max_retries = 10
    retry_delay = 1  # Initial retry delay in seconds
    for _ in range(max_retries):
        try:
            connection = mysql.connector.connect(
                host='algorise-mysql',
                user='root',
                password='0000',
                database='GeoJobSearch'
            )
            print("Connected to MySQL")
            return connection
        except mysql.connector.Error as e:
            print(f"Error while connecting to MySQL: {e}")
            print(f"Retrying in {retry_delay} seconds...")
            time.sleep(retry_delay)
            retry_delay *= 2  # Exponential backoff
    raise ValueError("Failed to connect to MySQL after multiple attempts")


def get_geocoded_address(company_name, city, province):
    """
    Constructs a full address string from the provided company name, city, and province,
    and returns the geocoded latitude, longitude, and formatted address.

    Parameters:
    - company_name (str): The name of the company.
    - city (str): The city where the company is located.
    - province (str): The province where the company is located.

    Returns:
    - tuple: A tuple containing the latitude, longitude, and formatted address obtained from geocoding the constructed address. If geocoding fails, None values are returned for each component of the tuple.
    """

    _address = f"{company_name}, {city}, {province}"
    return geocode_address(_address)


def insert_company_if_not_exists(connection, company_uid, company_name, city, province):
    """
    Inserts a new company into the Company table with geolocation data obtained through geocoding,
    but only if the company does not already exist in the database. The function checks the company's
    existence by its unique identifier (CompanyUID). If the city or province is marked as 'Remote',
    the company is inserted without geolocation data.

    Parameters:
    - connection: The MySQL database connection object.
    - company_uid (str): The unique identifier for the company.
    - company_name (str): The name of the company.
    - city (str): The city where the company is located. If 'Remote', geolocation data is not obtained.
    - province (str): The province where the company is located. If 'Remote', geolocation data is not obtained.

    The function attempts to geocode the company's address if the city and province are not marked as 'Remote'.
    If geocoding is successful, the latitude, longitude, and formatted address are included in the database insertion.
    If the company already exists in the database, insertion is skipped to avoid duplicates.
    """
    try:
        with connection.cursor() as cursor:
            check_query = "SELECT COUNT(*) FROM Company WHERE CompanyUID = %s"
            cursor.execute(check_query, (company_uid,))
            exists = cursor.fetchone()[0]

            if exists == 0:
                # Only attempt geocoding if the location is not marked as 'Remote'
                if city != "Remote" and province != "Remote":
                    try:
                        lat, lng, formatted_address = get_geocoded_address(
                            company_name, city, province)
                        if lat is not None and lng is not None:
                            insert_query = """
                            INSERT INTO Company (CompanyUID, Name, City, Province, Address, Latitude, Longitude)
                            VALUES (%s, %s, %s, %s, %s, %s, %s)
                            """
                            cursor.execute(
                                insert_query, (company_uid, company_name, city, province, formatted_address, lat, lng))
                        else:
                            # Handle the case where geocoding did not return valid coordinates
                            print(
                                f"Failed to geocode address for {company_name}.")
                    except ImportError as e:
                        print(
                            f"Error during geocoding for {company_name}: {e}")
                else:
                    # Insert without geolocation data if the location is 'Remote'
                    insert_query = """
                    INSERT INTO Company (CompanyUID, Name, City, Province)
                    VALUES (%s, %s, %s, %s)
                    """
                    cursor.execute(insert_query, (company_uid,
                                   company_name, city, province))

                # print(f"Company {company_name} inserted successfully.")
            else:
                print(
                    f"Company {company_name} already exists. Skipping insertion.")
    except mysql.connector.Error as e:
        print(f"Error inserting company {company_name}: {e}")


def insert_all_jobs(jobs):
    """
    Inserts multiple job listings into the 'Jobs' table in the 'GeoJobSearch' database.

    This function attempts to insert each job listing from a provided list into the database.
    Before insertion, it checks if the job's unique identifier (JobUID) already exists in the table
    to prevent duplicate entries. It reports the total number of jobs processed, successfully added,
    and the number of duplicates found.

    Parameters:
        jobs (list of dict): A list of dictionaries, where each dictionary contains the details
        of a job listing to be inserted into the database. Expected keys in each dictionary include
        'JobUID', 'Title', 'Company', 'Location', 'City', 'Province', 'Description', 'Salary',
        'JobType', 'Date', and 'URL'.

    Note:
        The database connection parameters are hardcoded. Adjust these as necessary to match your
        database configuration. Assumes the 'Jobs' table schema includes a 'JobUID' column.
    """
    total_jobs = len(jobs)
    jobs_added = 0

    try:
        # Establishing the connection
        connection = mysql.connector.connect(
            host="algorise-mysql",
            user="root",
            password="0000",
            database="GeoJobSearch",
            # charset='utf8mb4'  # Set the charset explicitly
        )

        with connection.cursor() as cursor:

            for job in jobs:
                # Ensure the company exists in the Company table
                insert_company_if_not_exists(
                    connection, job['CompanyUID'], job['Company'], job['City'], job['Province'])

                # Check if a job with the same JobUID already exists
                select_query = "SELECT EXISTS(SELECT 1 FROM Jobs WHERE JobUID = %s)"
                cursor.execute(select_query, (job['JobUID'],))
                exists = cursor.fetchone()[0]
                if exists:
                    print(
                        f"==DUPLICATE-INFO== Job with JobUID {job['JobUID']} already exists. Skipping insertion.")
                    # Here, you could also choose to update the existing record instead of skipping
                    continue

                # SQL statement for inserting a job
                sql = """
                INSERT INTO Jobs (JobUID, Title, Company, CompanyUID, Location, City, Province, Description, Salary, JobType, Date, JobURL)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"""
                # Prepare data tuple for the job
                job_data = (
                    job['JobUID'], job['Title'], job['Company'], job['CompanyUID'], job['Location'],
                    job['City'], job['Province'], job['Description'], job['Salary'],
                    job['JobType'], job['Date'], job['URL']
                )
                # Execute the SQL command
                cursor.execute(sql, job_data)

                jobs_added += 1  # update the jobs added count

        # Commit changes
        connection.commit()

        # Confirmation message
        print(f"==INFO== {jobs_added} jobs inserted successfully!!!")

    except mysql.connector.Error as e:
        # Error handling
        print("Error while connecting to MariaDB:", e)
        # Rollback changes if an error occurs
        if connection:
            connection.rollback()

    finally:
        # Closing database connection
        if 'connection' in locals():
            connection.close()
            print("==INFO== MariaDB connection closed")

    duplicates_found = total_jobs - jobs_added
    print(
        f"==SUMMARY-INFO== {jobs_added}/{total_jobs} jobs were added to the database. {duplicates_found} duplicates found and omitted.")
