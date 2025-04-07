"""
Web Scraping script for Indeed Job Listings
This project's approach to web scraping was referenced from  a tutorial by  DataHut,
which focuses on the use of Selenium and BeautifulSoup for extracting job listings from Indeed.
Tutorial details are available at:
https://www.blog.datahut.co/post/scrape-indeed-using-selenium-and-beautifulsoup
The development of docstring documentation was also assisted by ChatGPT, an AI developed by OpenAI.
"""
import time
import re
import json
import os
import logging
from datetime import datetime, timedelta

from bs4 import BeautifulSoup
from lxml import etree
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager

import job_processor

# Set up logging
logger = logging.getLogger("web_scraper")
logger.setLevel(logging.DEBUG)

# Check if the log file already exists to append instead of overwriting
if not os.path.exists("web_scraper_logs.txt"):
    MODE = "w"
else:
    MODE = "a"

fh = logging.FileHandler("web_scraper_logs.txt", mode=MODE)
fh.setLevel(logging.DEBUG)
formatter = logging.Formatter(
    "%(asctime)s - %(name)s - %(levelname)s - %(message)s", datefmt="%Y-%m-%d %H:%M:%S"
)
fh.setFormatter(formatter)
logger.addHandler(fh)


def initialize_webdriver():
    """Initialize driver"""
    chrome_options = Options()
    chrome_options.add_argument("--headless")
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--disable-dev-shm-usage")
    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--window-size=1920x1080")
    # Adding a user-agent string
    chrome_options.add_argument(
        "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36"
    )

    driver_1 = webdriver.Chrome(
        service=Service(ChromeDriverManager().install()), options=chrome_options
    )
    return driver_1


def geturl(searchparams_1, targetwebsites, pagenumber):
    """
    constructs a page URL template for Indeed job searches.

    Args:

        searchParams: a dictionary of search parameters
        targetWebsites: a dictionary of target websites and their specifications
        pageNumber: page number for job search ur;

    Returns:
        pageUrl: A string representing a formatted URL for the given website.
    """

    pageurl = targetwebsites[0]["template_url"]
    keyword = searchparams_1[0]["keyword"]
    location = searchparams_1[0]["location"]
    radius = searchparams_1[0]["radius"]
    fromage = searchparams_1[0]["fromage"]
    sort = searchparams_1[0]["sort"]
    startnum = searchparams_1[0]["start"]

    # return url for first page of search resultss
    if pagenumber == 0:

        pageurl = pageurl.format(keyword, location, radius, fromage, sort, startnum)

    # return page number for desired page of search results
    else:
        pageurl = pageurl.format(keyword, location, radius, fromage, sort, pagenumber)

    return pageurl

def createjob(title, company, date, joblocation, insight, jobdescription, joblink):
    """
    Creates a dictionary representing an improperly formatted job listing with provided details.

    Args:
        title (str): The Job title
        company (str): The company name
        date (str): The date the job was posted.
        jobLocation (str): Job Location
        insight (list): A list of insights related to the job(salary, jobtype).
        jobDescription (str): Job Description
        jobLink (str): A URL link to the job posting.

    Returns:
        dict: A dictionary containing details of the job listing.
    """

    joblist = {
        "title": title,
        "company": company,
        "date": date,
        "location": joblocation,
        "insights": insight,
        "description": jobdescription,
        "url": joblink,
    }

    return joblist


def getjoblink(job):
    """
    Extracts the job link from a job element using XPath.

    Attempts to find the href attribute of an anchor tag within an h2 element. If the link
    cannot be found due to an exception, it returns a default 'Not available' string.

    Args:
        job: An lxml element representing a job listing.

    Returns:
        str: The extracted href link as a string if found, otherwise 'Not available'.
    """
    try:
        job_link = job.xpath("./descendant::h2/a/@href")[0]

    except IndexError:
        job_link = "Not available"

    return job_link


def getpagedom(url, driver_1):
    """
    Retrieves the DOM from a given URL using Selenium and parses it with BeautifulSoup and lxml.

    Navigates to a URL using a Selenium WebDriver, then captures the page source and returns it

    Args:
        url (str): The URL of the webpage to be accessed.
        driver (selenium.webdriver): The Selenium WebDriver used to navigate.

    Returns:
        page_content: The HTML content of the page
    """
    driver_1.get(url)
    page_content = driver_1.page_source
    return page_content


def getdom(url, driver_1):
    """
    Retrieves the DOM from a given URL using Selenium and parses it with BeautifulSoup and lxml.

    Navigates to a URL using a Selenium WebDriver, then captures the page source and parses it
    first with BeautifulSoup to handle HTML content, and then with lxml for XPath queries.

    Args:
        url (str): The URL of the webpage to be accessed.
        driver (selenium.webdriver): The Selenium WebDriver used to navigate.

    Returns:
        lxml.etree._Element: The parsed DOM of the page, suitable for XPath queries.
    """
    driver_1.get(url)
    page_content = driver_1.page_source
    product_soup = BeautifulSoup(page_content, "html.parser")
    dom = etree.HTML(str(product_soup))
    return dom


# Extract Job Title
def getjobtitle(job):
    """
    Extracts the job title from a job listing element.

    Tries to retrieve the job title text from a specified XPath within the job element.
    If the operation fails due to an exception, it defaults to 'Not available'.

    Args:
        job: An lxml element representing a job listing.

    Returns:
        str: The extracted job title or 'Not available' if extraction fails.
    """
    try:
        jobtitle = job.xpath("./descendant::h2/a/span/text()")[0]
    except IndexError:
        jobtitle = "Not available"
    return jobtitle


# Extract Company Name
def getcompanyname(job):
    """
    Extracts the company name from a job listing element.
    Attempts to retrieve the company name using an XPath query targeting a span element with a specific 'data-testid'.
    If the company name cannot be found due to an exception, it defaults to 'Not available'.

    Args:
        job: An lxml element representing a job listing, used to perform the XPath query.

    Returns:
        str: The extracted company name or 'Not available' if the extraction fails.
    """
    try:
        companyname = (job.xpath(".//span[@data-testid='company-name']")[0]).text
    except IndexError:
        companyname = "Not available"
    return companyname


# functions to extract the company location
def getcompanylocation(job):
    """
    Retrieves the location of the company from a job listing element.

    This function attempts to find the company's location by querying an lxml element,
    for a specific 'data-testid' attribute. If the query fails due to an exception,
    a default value indicating that the location is not available is returned.

    Args:
        job: An lxml element representing a single job listing, used to perform the XPath query.

    Returns:
        str: The company location as a string, or "Not available" if the extraction fails.
    """
    try:
        companylocation = job.xpath(".//div[@data-testid='text-location']")[0].text

        if companylocation is None:
            companylocation = job.xpath(
                ".//div[@data-testid='text-location']/span/text()"
            )[0]

    except IndexError:
        companylocation = "Not available"

    return companylocation


# Extract insights such as job type, salary, and workdays
def getinsights(job):
    """
    Extracts insightssuch as job type, salary, and workdays from a job listing element.

    Gathers insights from a given job element based on a specific 'data-testid' attribute. It attempts
    to find all matching div elements and collects their text content. If the operation fails due to
    an exception, it returns a default message indicating insights are not available.

    Args:
        job: An lxml element representing a job listing, expected to contain insight divs.

    Returns:
        list: A list of extracted insights as text strings or 'Not available' if an exception occurs.
    """

    insights = []
    try:
        insightslisttemp = job.xpath(".//div[@data-testid='attribute_snippet_testid']")

        for field in insightslisttemp:
            insights.append(field.text)

    except IndexError:
        insights = "Not available"

    return insights


# Extract job description
def getjobdescription(job):
    """
    Extracts the job description from a job listing element.

    Tries to retrieve the job description text with the html tags from the 'jobdescriptionText' div
    within a given job listing element using XPath. If successful, returns the formatted text
    else, returns a default message indicating the description is not available.

    Args:
        job: HTML content of the page

    Returns:
        str: The cleaned job description text or a default message if not available.
    """

    try:

        # Parse the HTML content using BeautifulSoup
        product_soup = BeautifulSoup(job, "html.parser")

        # Find the job description element
        jobdescription = product_soup.find("div", id="jobdescriptionText")

        # extract Job description text with preserved HTML tags
        jobdescription = "".join(str(child) for child in jobdescription.children)

    except IndexError:

        jobdescription = ["Not available"]

    return jobdescription


def getjobdate(job):
    """
    Extracts the job posting date from a job listing element.

    Attempts to retrieve the posting date text from the specified.
    If successful, returns the text describing the original posting date.
    If an exception occurs, such as when the date is not available, it returns
    "Not available."

    Args:
        job: An lxml element representing a job listing.

    Returns:
        str: The job posting date text or "Not available" if not found.
    """

    try:
        postingdate = job.xpath('.//span[@data-testid="myJobsStateDate"]/text()')

    except Exception: # pylint:disable=broad-except

        postingdate = "Not available"

    return postingdate


def formatjobdate(jobdatestring):
    """
    Format job posting date from a given date string.

    Parses a date string in various formats commonly found in job listings and
    returns a formatted date string in 'YYYY-MM-DD' format.

    Args:
        JobDateString (str): The date string to be formatted.
    Returns:
        str: A formatted date string in 'YYYY-MM-DD' format or the original
        date string if it cannot be parsed.
    """

    datestring = jobdatestring[0]

    # date extracted is in format "Posted X days ago"
    if "Posted" in datestring:

        # Extract the number of days from the original job posting
        dayselapsedlist = re.findall(r"\d+", datestring)

        dayselapsed = int(dayselapsedlist[0])
        postingdate = datetime.today() - timedelta(days=dayselapsed)

    # date extracted is in format 'Just Posted' or 'Today
    elif "Just posted" in datestring or "Today" in datestring:

        postingdate = datetime.today()

    # date is formated as "Active X days ago"
    else:

        postingdatestring = datestring
        return postingdatestring

    # format date
    postingdatestring = postingdate.strftime("%Y-%m-%d")

    return postingdatestring


def parsepage(targetwebsites, searchparams, driver_1, baseurl, maxnumpages, step_1, start_1):
    """
    Parses job listings from a specified URL and stores them in a list

    Iterates through a list of job keywords and provinces, fetching job listings from Indeed
    across multiple pages. Extracts details such as job link, title, company name, location,
    insights, and description for each job, and compiles them into a list of dictionaries.

    Args:
        pageUrl (str): URL template for fetching job listings.
        Keyword (list of str): List of job keywords to search for.
        province (str): The province where the job search is focused.
        driver (selenium.webdriver): The Selenium WebDriver instance used for web navigation.
    """

    alljobs = []
    pagenumber = start_1

    # Initialize a counter for the pages processed
    pages_processed = 0

    # iterate through multiple pages
    while pagenumber < maxnumpages * step_1:
        # format url
        url = geturl(searchparams, targetwebsites, pagenumber)
        pagedom = getdom(url, driver_1)
        jobs = pagedom.xpath('//div[@class="job_seen_beacon"]')
        # print(jobs)
        alljobs = alljobs + jobs
        # go to next page
        pagenumber += step_1
        pages_processed += 1  # Increment the pages processed counter
        time.sleep(4)

    # After exiting the loop, print the number of jobs found and the number of pages checked
    print(f"{len(alljobs)} jobs were found after checking {pages_processed} pages.")
    logger.info("%s jobs were found after checking %s pages.", len(alljobs), pages_processed)

    # Folder name
    folder_name = "job_lists_json"

    # Ensure the folder exists job_lists_json folder
    if os.path.exists(folder_name):
        # stores the file in the
        filename_1 = os.path.join(
            folder_name,
            datetime.today().strftime("%Y-%m-%d") + "-Job-listings" + ".txt",
        )

    with open(filename_1, "w", encoding="utf-8") as file_1:
        jobs_written = 0  # Counter for the number of jobs written to the file
        print("==INFO== Processing all jobs")
        # print(allJobs)

        for job in alljobs:
            # print(job)
            job_link = baseurl + getjoblink(job)  # extract job link
            job_title = getjobtitle(job)  # extract fields
            company_location = getcompanylocation(job)
            company_name = getcompanyname(job)
            job_insight = getinsights(job)
            job_date = getjobdate(job)
            formated_job_date = formatjobdate(job_date)  # format the job posting date
            # navigate to each job posting's page
            job_dom = getpagedom(job_link, driver_1)
            # extract description unformatted
            job_description = getjobdescription(job_dom)

            # create job object
            newjob = createjob(
                job_title,
                company_name,
                formated_job_date,
                company_location,
                job_insight,
                job_description,
                job_link,
            )

            # write directly to file
            json.dump(newjob, file_1, ensure_ascii=False, indent=4)
            file_1.write("\n")

            jobs_written += 1
            print(
                f"Job written to file: {job_title} at {company_name}"
            )  # Confirmation message

            # wait to limit server overload
            time.sleep(4)

    # close driver
    driver_1.quit()
    print("==INFO[parsePage]== Closing Web Driver...")

    return filename_1


if __name__ == "__main__":
    # Record the start time
    start_time = datetime.now()

    # Open configuration file
    with open("config.json", "r", encoding="utf-8") as file:
        scraperConfig = json.load(file)

    # Dictionary of search parameters
    searchParams = scraperConfig["search_settings"]
    # Dictionary of target websites and their specifications
    targetWebsite = scraperConfig["target_websites"]
    baseUrl = targetWebsite[0]["base_url"]  # Base URL for website
    maxNumPages = searchParams[0]["max_pages"]  # Page navigation information
    step = searchParams[0]["step"]
    start = searchParams[0]["start"]

    # Initialize WebDriver with headless configuration
    driver = initialize_webdriver()

    # Get the list of scraped jobs
    print("==INFO== Now scrapping some jobs !!!")
    logger.info("==INFO== Web Scraper Service Started... ")

    logger.info("==INFO== Webscraping Initialized... ")
    # The function should return the filename/location of the new data scrapped
    filename = parsepage(
        targetWebsite, searchParams, driver, baseUrl, maxNumPages, step, start
    )
    logger.info("==INFO== Webscraping Completed... ")
    logger.info("==INFO== Scrapped data saved to: %s", filename)

    # Base directory for application
    APP_BASE_DIR = "/app"

    # Get the full filename path
    filename_full_path = os.path.join(APP_BASE_DIR, filename)
    print(f"    ==INFO== Job data saved to: {filename_full_path}")

    # 1. Convert the file into a JSON Array
    JSON_LIST_FILEPATH = job_processor.update_file_with_json_array(filename_full_path)

    # Validation step
    if os.path.exists(JSON_LIST_FILEPATH) and os.path.getsize(JSON_LIST_FILEPATH) > 0:
        # File exists and is not empty, now check if it contains a JSON array of objects
        with open(JSON_LIST_FILEPATH, "r", encoding="utf-8") as file:
            try:
                data = json.load(file)
                if isinstance(data, list) and len(data) > 0:
                    print(
                        f"==VALIDATION== JSON list file {JSON_LIST_FILEPATH} is valid and contains {len(data)} entries."
                    )
                else:
                    print(
                        f"==VALIDATION== JSON list file {JSON_LIST_FILEPATH} does not contain a valid list of job entries."
                    )
            except json.JSONDecodeError:
                print(
                    f"==VALIDATION== JSON list file {JSON_LIST_FILEPATH} could not be decoded. Please check the file format."
                )
    else:
        print(
            f"==VALIDATION== JSON list file {JSON_LIST_FILEPATH} is empty or does not exist."
        )

    print(f"==INFO== Successfully converted {JSON_LIST_FILEPATH} into a JSON Array")
    logger.info("==INFO== Successfully converted %s into a JSON Array", JSON_LIST_FILEPATH)

    # 2. Append the job the database
    # sends the JSON array for processing into the db
    job_processor.main_job_processor(JSON_LIST_FILEPATH)
    print(f"==INFO== Successfully appended {JSON_LIST_FILEPATH} into database")
    logger.info("==INFO== Successfully appended %s into database", JSON_LIST_FILEPATH)

    # 3. Append the job to the legacy job listings
    LEGACY_FILE_PATH = r"/app/job_lists_json/legacy-job-listings.txt"
    job_processor.append_json_array_from_source_to_target(
        JSON_LIST_FILEPATH, LEGACY_FILE_PATH
    )  # Archives the processed jobs
    print("==INFO== Successfully appended {JSON_LIST_FILEPATH} to the legacy job listings {legacy_file_path}")
    logger.info("==INFO== Successfully appended %s to the legacy job listings %s", JSON_LIST_FILEPATH, LEGACY_FILE_PATH)

    # Record the end time
    end_time = datetime.now()

    # Calculate and log the elapsed time
    elapsed_time = end_time - start_time
    print(f"==INFO== Scrapping and processing completed in {elapsed_time}.")
    logger.info("==INFO== Scrapping and processing completed in %s", elapsed_time)
    logger.info("==INFO== Web Scraper Service Ended... \n")
    print("==INFO== Web Scraper Service Ended... \n")
