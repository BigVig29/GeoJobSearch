"""module importing json, time, and db connection"""
import json
from datetime import datetime, timedelta
import re
import os
import db_conn_util


def update_file_with_json_array(file_path):
    """
    Reads a continuous block of JSON objects from a file, aggregates them into a list,
    and writes this list to a new file as a JSON array. The new file is created in the
    same directory as the source file, with a name based on the source file name appended
    with "_json-list" and the current timestamp.

    Parameters:
    - file_path (str): The path to the input file to be read.
    """
    # Determine the output directory and the base name of the source file
    output_dir = os.path.dirname(file_path)
    base_name = os.path.splitext(os.path.basename(file_path))[0]

    # Generate a unique filename for the output file based on the source file name and current timestamp
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    output_file_name = f"{base_name}_json-list_{timestamp}.txt"
    output_file_path = os.path.join(output_dir, output_file_name)

    # Initialize a list to store the aggregated JSON objects
    objects_list = []

    # Read the entire file content
    with open(file_path, 'r', encoding='utf-8') as file:
        # Split the file content at '}{' with adjustment for proper JSON format
        json_objects = file.read().strip().split('}\n{')

    # Process each JSON object string
    for index, obj_str in enumerate(json_objects):
        # Properly bracket each split JSON string if necessary
        if not obj_str.startswith('{'):
            obj_str = '{' + obj_str
        if not obj_str.endswith('}'):
            obj_str += '}'

        # Attempt to decode each JSON string into a dictionary
        try:
            obj = json.loads(obj_str)
            objects_list.append(obj)
        except json.JSONDecodeError as e:
            print(f"Error decoding JSON: {e} at index {index}")

    # Write the list of JSON objects to the new file
    with open(output_file_path, 'w', encoding='utf-8') as outfile:
        json.dump(objects_list, outfile, indent=4, ensure_ascii=False)

    return output_file_path


def append_json_array_from_source_to_target(source_file, target_file):
    """
    Reads a JSON array from a source file and appends it to a JSON array in a target file.

    This function performs several checks and actions:
    - Verifies if the source file exists. If not, it exits the function with a message.
    - Reads the JSON array from the source file.
    - Checks if the target file exists. If the target file does not exist, it creates a new file with an empty JSON array.
    - Reads the existing JSON array from the target file (if it exists).
    - Appends the source JSON array to the target JSON array.
    - Writes the updated JSON array back to the target file.

    Parameters:
    - source_file (str): The path to the source file containing the JSON array to append.
    - target_file (str): The path to the target file to which the JSON array from the source file will be appended.

    Returns:
    - None: The function does not return a value but writes the updated JSON array to the target file.
    """
    # Check if the source file exists
    if not os.path.exists(source_file):
        print(f"Source file does not exist: {source_file}")
        return
    # Read the source JSON array
    with open(source_file, 'r', encoding='utf-8') as source_f:
        source_data = json.load(source_f)
    # Check if the target file exists, create an empty array if it does not
    if not os.path.exists(target_file):
        with open(target_file, 'w', encoding='utf-8') as target_f:
            json.dump([], target_f)
    # Read the target JSON array
    with open(target_file, 'r', encoding='utf-8') as target_f:
        target_data = json.load(target_f)
    # Append the source array to the target array
    target_data.extend(source_data)
    # Write the updated array back to the target file
    with open(target_file, 'w', encoding='utf-8') as target_f:
        json.dump(target_data, target_f, indent=4)


def remove_header_tags(description):
    """
    Remove HTML header tags (e.g., <h1>, <h2>, etc.) from a job description.

    Parameters:
    - description (str): The job description text with HTML content.

    Returns:
    - str: The job description text with header tags removed.
    """

    # Regular expression pattern to match
    # all header tags (h1, h2, h3, etc.) from the job descriptions
    header_pattern = r'<h\d\s*[^>]*>.*?<\/h\d>'

    # Use re.sub() to remove all occurrences of the pattern
    cleaned_description = re.sub(header_pattern, '', description)

    return cleaned_description


def parse_location(location):
    """
    Parse a job's location string to extract and return the city and province.

    Parameters:
    - location (str): The location string, which can be in various formats.

    Returns:
    - tuple: A tuple containing the city and province. If the format is unrecognized or information is missing, "NA" is used as a placeholder.
    """

    # Handle edge case for "Remote"
    if location.strip().lower() == "remote":
        return "Remote", "Remote"

    # Handle edge case for "Hybrid remote in"
    hybrid_match = re.match(r"Hybrid remote in (.+), (.+)", location)
    if hybrid_match:
        return hybrid_match.group(1), hybrid_match.group(2)

    # Handle edge case for "Remote in"
    remote_match = re.match(r"Remote in (.+), (.+)", location)
    if remote_match:
        return remote_match.group(1), remote_match.group(2)

    # Split city and province by comma
    parts = location.split(", ")
    if len(parts) == 2:
        return parts[0], parts[1]
    if len(parts) == 1:
        return parts[0], "NA"  # If only city is provided, set province to "NA"
    return "NA", "NA"  # If location format is invalid, set both city and province to "NA"


def extract_salary(insights):
    """
    Extract salary information from job insights. Supports various formats like yearly, monthly, hourly, and ranges thereof.

    Parameters:
    - insights (list): A list of strings, where one of the strings potentially contains salary information.

    Returns:
    - int or None: The calculated average salary as an integer if salary information is found and recognizable; otherwise, None.
    """

    # Compile regular expression patterns
    yearly_pattern = re.compile(r'\$([\d,]+) a year')
    hourly_range_pattern = re.compile(r'\$([\d.]+)–\$([\d.]+) an hour')
    hourly_single_pattern = re.compile(r'\$([\d.]+) an hour')
    monthly_range_pattern = re.compile(r'\$([\d,]+)–\$([\d,]+) a month')
    yearly_range_pattern = re.compile(r'\$([\d,]+)–\$([\d,]+) a year')
    up_to_hourly_pattern = re.compile(r'Up to \$([\d.]+) an hour')

    # Check if insights contain salary information
    if len(insights) <= 2:
        return None

    # Get the first item from insights list
    salary_insight = insights[0].strip()

    # Match each pattern to the salary insight
    patterns = [
        (yearly_pattern, lambda m: int(m.group(1).replace(',', ''))),
        (hourly_range_pattern, lambda m: int(
            ((float(m.group(1)) + float(m.group(2))) / 2) * 40 * 52)),
        (hourly_single_pattern, lambda m: int(float(m.group(1)) * 40 * 52)),
        (monthly_range_pattern, lambda m: int(((int(m.group(1).replace(',', '')) +
                                                int(m.group(2).replace(',', ''))) / 2) * 12)),
        (yearly_range_pattern, lambda m: int(((int(m.group(1).replace(',', '')) +
                                               int(m.group(2).replace(',', ''))) / 2))),
        (up_to_hourly_pattern, lambda m: int(float(m.group(1)) * 40 * 52))
    ]

    for pattern, conversion_func in patterns:
        match = pattern.match(salary_insight)
        if match:
            return conversion_func(match)

    return None  # Return None if salary info format not recognized


def preprocess_jobs(json_file1):
    """
    Preprocess jobs data from a JSON file. Extracts and cleans job information, including title, company, location, description, salary, and job type.

    Parameters:
    - json_file (str): Path to the JSON file containing job listings.

    Returns:
    - list: A list of dictionaries, each representing a job with cleaned and extracted information.
    """
    with open(json_file1, 'r', encoding='utf-8') as f:
        data = json.loads(f.read())
        jobs1 = []
        for job_data in data:
            # Skip the job if the description is a list
            if isinstance(job_data["description"], list):
                continue

            # Process the job insights
            insights = job_data.get("insights", [])

            if len(insights) > 2:
                salary = extract_salary(insights)
            else:
                continue

            if salary is not None:

                # Process the descriptions
                cleaned_description = remove_header_tags(
                    job_data["description"])
                # Process the locations
                city, province = parse_location(job_data["location"])

                # Generate JobUID using title, company, city, and province without spaces and in lowercase
                job_title = job_data["title"].title().replace(' ', '').lower()
                company = job_data["company"].replace(' ', '').lower()
                city_formatted = city.replace(' ', '').lower()
                province_formatted = province.replace(' ', '').lower()
                job_uid = f"{job_title}_{company}_{city_formatted}_{province_formatted}"
                # Generate CompanyUID using company, city, and province without spaces and in lowercase
                company_uid = f"{company}_{city_formatted}_{province_formatted}"

                # Extracting the job type from insights list
                job_type = insights[1] if len(insights) > 1 else "NA"
                # Parsing the date field
                # job_data = {"date": "2024-03-15"} or job_data = {"date": "Active 2 days ago"}
                if "date" in job_data:
                    try:
                        # Attempt to parse the date using the expected format
                        date_obj = datetime.strptime(
                            job_data["date"], "%Y-%m-%d")
                        # Convert datetime object to string
                        date = date_obj.strftime('%Y-%m-%d')
                    except ValueError:
                        # If parsing fails, check for the "Active X days ago" pattern
                        match = re.search(
                            r'Active (\d+) days ago', job_data["date"])
                        if match:
                            # Extract the number of days from the match object
                            days_ago = int(match.group(1))
                            # Calculate the date as a datetime object
                            date_obj = datetime.now() - timedelta(days=days_ago)
                            # Convert the datetime object to a string
                            date = date_obj.strftime('%Y-%m-%d')
                        else:
                            # If the string does not match any expected pattern, set date to "NA"
                            date = "NA"
                else:
                    # If the "date" key does not exist, use "NA"
                    date = "NA"
                url = job_data.get("url", "NA")

                job = {
                    "JobUID": job_uid,
                    "Title": job_data["title"].title(),
                    "Company": job_data["company"],
                    "CompanyUID": company_uid,
                    "Location": job_data["location"],
                    "City": city,
                    "Province": province,
                    "Description": cleaned_description,
                    "Salary": salary,
                    "JobType": job_type,
                    "Date": date,
                    "URL": url
                }
                jobs1.append(job)

    # print(jobs)
    print(
        f"==INFO[preprocess_jobs]== The total number of jobs after pre-processing is: {len(jobs1)}")
    return jobs1


def main_job_processor(json_file_path):
    """function to define the main processor"""
    print(
        f"==INFO[main_job_processor]== STEP-1: Processing new jobs from {json_file_path} for database...")
    json_file = json_file_path
    # 1. Parse the .txt file
    jobs_main = preprocess_jobs(json_file)

    # # 2. Reset the db connection
    print("==INFO[main_job_processor]== STEP-2: Testing database connectivity...")
    db_conn_util.connect_to_mysql()  # Test the connection to the db
    # db_conn_util.reset_database() # This would drop all row in the db

    # 3. Add the jobs to the db
    print(
        "==INFO[main_job_processor]== STEP-3: Inserting all new jobs to the database...")
    print(jobs_main)
    db_conn_util.insert_all_jobs(jobs_main)


if __name__ == "__main__":
    print("==INFO== STEP-1: Processing new jobs for database...")
    JSON_FILE = r"/app/job_lists_json/legacy-job-listings.txt"
    # 1. Parse the .txt file
    jobs = preprocess_jobs(JSON_FILE)

    # # 2. Reset the db connection
    print("==INFO== STEP-2: Testing database connectivity...")
    db_conn_util.connect_to_mysql()  # Test the connection to the db
    # db_conn_util.reset_database() # This would drop all row in the db

    # 3. Add the jobs to the db
    print("==INFO== STEP-3: Inserting all new jobs to the database...")
    db_conn_util.insert_all_jobs(jobs)
