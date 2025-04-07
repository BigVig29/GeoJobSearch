"""Modules to import functions tested"""
from datetime import datetime
from unittest.mock import MagicMock
import pytest
from selenium.webdriver import Chrome
from lxml import etree
from web_scraper import (
    geturl,
    createjob,
    getjoblink,
    getpagedom,
    getdom,
    getjobtitle,
    getcompanyname,
    getcompanylocation,
    getinsights,
    getjobdescription,
    getjobdate,
    formatjobdate,
)

# Test get_Url
@pytest.fixture(name="search_params")
def search_params_fixture():
    """Define search parameters"""
    # Sample search parameters
    return [{
        "keyword": "python",
        "location": "New York",
        "radius": 10,
        "fromage": 7,
        "sort": "date",
        "start": 0
    }]


@pytest.fixture(name="target_websites")
def target_websites_fixture():
    """Define target websites"""
    # Sample target websites and their specifications
    return [{
        "template_url": "https://www.example.com/search?keyword={}&location={}&radius={}&fromage={}&sort={}&start={}"
    }]


def test_geturl_first_page(search_params, target_websites):
    """Test get url"""
    # Testing URL construction for the first page
    page_url = geturl(search_params, target_websites, pagenumber=0)
    assert page_url == "https://www.example.com/search?keyword=python&location=New York&radius=10&fromage=7&sort=date&start=0"


# Test createJob function
def test_createjob():
    """Test create job"""
    # Sample input parameters
    title = "Software Engineer"
    company = "ABC Company"
    date = "2024-03-19"
    job_location = "New York"
    insight = ["$100,000", "Full-time"]
    job_description = "This is a description of the job."
    job_link = "https://www.example.com/job123"

    # Call the function
    job_dict = createjob(title, company, date, job_location,
                         insight, job_description, job_link)

    # Assertions to validate the returned dictionary
    assert job_dict["title"] == title
    assert job_dict["company"] == company
    assert job_dict["date"] == date
    assert job_dict["location"] == job_location
    assert job_dict["insights"] == insight
    assert job_dict["description"] == job_description
    assert job_dict["url"] == job_link

# Test getjoblink function


def test_getjoblink_with_link():
    """Test get job with link"""
    # Create an lxml element representing a job listing with a link
    job_element = etree.fromstring(
        '<job><h2><a href="https://www.example.com/job123">Job Title</a></h2></job>')

    # Call the function
    job_link = getjoblink(job_element)

    # Assert that the extracted link matches the expected value
    assert job_link == "https://www.example.com/job123"


def test_getjoblink_without_link():
    """Test get job without link"""
    # Create an lxml element representing a job listing without a link
    job_element = etree.fromstring('<job><h2>Job Title</h2></job>')

    # Call the function
    job_link = getjoblink(job_element)

    # Assert that the function returns 'Not available' when there's no link
    assert job_link == "Not available"

# Test getPageDom
@pytest.fixture(name="mock_driver")
def mock_driver_fixture():
    """Define mock driver"""
    # Mock the Selenium WebDriver
    driver_mock = MagicMock()
    return driver_mock


def test_get_page_dom(mock_driver):
    """Test get page dom"""
    # Mock the URL
    url = "https://example.com"

    # Mock the page source
    page_source = "<html><body><h1>Hello, World!</h1></body></html>"
    mock_driver.page_source = page_source

    # Call the function
    result = getpagedom(url, mock_driver)

    # Assertions
    assert result == page_source
    mock_driver.get.assert_called_once_with(url)
    mock_driver.page_source = page_source


# Test getDom function (mocking WebDriver)
@pytest.fixture(name="mocked_driver")
def mocked_driver_fixture(mocker):
    """Define mock driver fixture"""
    # Mocking the Selenium WebDriver
    driver_mock = mocker.MagicMock(Chrome)
    driver_mock.page_source = "<html><head></head><body><div></div></body></html>"
    return driver_mock


def test_getdom(mocked_driver):
    """Test get dom"""
    # Mock URL
    url = "https://www.example.com"

    # Call the function
    dom = getdom(url, mocked_driver)

    # Assertions
    # pylint: disable=protected-access
    assert isinstance(dom, etree._Element)

# Test getjobtitle function
def test_get_job_title():
    """Test get job title"""
    # Test case for job title extraction
    job_element = etree.fromstring(
        '<div><h2><a><span>Job Title</span></a></h2></div>')
    title = getjobtitle(job_element)
    assert title == "Job Title"


def test_get_job_title_not_available():
    """Test get job title where job title is not available"""
    # Test case when job title extraction fails
    job_element = etree.fromstring('<div></div>')
    title = getjobtitle(job_element)
    assert title == "Not available"


# Test getcompanyname function
def test_get_company_name():
    """Test getting company name"""
    # Test case for company name extraction
    job_element = etree.fromstring(
        '<div><span data-testid="company-name">Company Name</span></div>')
    company_name = getcompanyname(job_element)
    assert company_name == "Company Name"


def test_get_company_name_not_available():
    """Test get company name where company name is not available"""
    # Test case when company name extraction fails
    job_element = etree.fromstring('<div></div>')
    company_name = getcompanyname(job_element)
    assert company_name == "Not available"


def test_get_company_name_empty():
    """Test get company name where company name is empty"""
    # Test case when company name is empty
    job_element = etree.fromstring(
        '<div><span data-testid="company-name"></span></div>')
    company_name = getcompanyname(job_element)
    assert company_name is None


def test_get_company_name_multiple_spans():
    """Test get company name along multiple spans"""
    # Test case when multiple span elements exist but only one has the correct data-testid attribute
    job_element = etree.fromstring(
        '<div><span>Other Data</span><span data-testid="company-name">Company Name</span></div>')
    company_name = getcompanyname(job_element)
    assert company_name == "Company Name"


def test_get_company_name_nested_span():
    """Test get company name along nested spans"""
    # Test case when the span with the company name is nested within other elements
    job_element = etree.fromstring(
        '<div><div><span data-testid="company-name">Company Name</span></div></div>')
    company_name = getcompanyname(job_element)
    assert company_name == "Company Name"


def test_get_company_name_no_data_testid():
    """Test get company name with no data test id"""
    # Test case when there's no span element with the data-testid attribute for company name
    job_element = etree.fromstring('<div><span>Other Data</span></div>')
    company_name = getcompanyname(job_element)
    assert company_name == "Not available"

# Test getcompanylocation function
def test_get_company_location():
    """Test getting company location"""
    # Test case for company location extraction
    job_element = etree.fromstring(
        '<div><div data-testid="text-location">Company Location</div></div>')
    company_location = getcompanylocation(job_element)
    assert company_location == "Company Location"


def test_get_company_location_span():
    """Test get company location with span"""
    # Test case when company location is within a span element
    job_element = etree.fromstring(
        '<div><div data-testid="text-location"><span>Location</span></div></div>')
    company_location = getcompanylocation(job_element)
    assert company_location == "Location"


def test_get_company_location_not_available():
    """Test get company location not available"""
    # Test case when company location extraction fails
    job_element = etree.fromstring('<div></div>')
    company_location = getcompanylocation(job_element)
    assert company_location == "Not available"

# Test getInsights function
def test_get_insights():
    """Test getting insights"""
    # Test case for extracting insights
    job_element = etree.fromstring('''
        <div>
            <div data-testid="attribute_snippet_testid">Job Type: Full-time</div>
            <div data-testid="attribute_snippet_testid">Salary: $50,000 - $70,000 per year</div>
            <div data-testid="attribute_snippet_testid">Workdays: Monday to Friday</div>
        </div>
    ''')
    insights = getinsights(job_element)
    assert insights == ['Job Type: Full-time',
                        'Salary: $50,000 - $70,000 per year', 'Workdays: Monday to Friday']


def test_get_insights_not_available():
    """Test insights not available"""
    # Test case when extraction fails
    job_element = etree.fromstring('<div></div>')
    insights = getinsights(job_element)
    assert insights == []

# Test case for when job description is available
def test_job_description_available():
    """Test to get job description"""
    job_html = """
    <html>
        <body>
            <div id="jobdescriptionText">
                <p>This is a sample job description.</p>
            </div>
        </body>
    </html>
    """
    expected_description = "\n<p>This is a sample job description.</p>\n"
    assert getjobdescription(job_html) == expected_description

def test_getjobdescription_without_description():
    """No job description"""
    # HTML content without job description
    html_content = """
    <html>
        <body>
            <div id="jobdescriptionText">
                <!-- No job description available -->
            </div>
        </body>
    </html>
    """

    expected_description = "\n No job description available \n"
    assert getjobdescription(html_content) == expected_description


# Test getJobDate function
def test_get_job_date():
    """Test getting job date"""
    # Test case for extracting job date
    job_element = etree.fromstring(
        '<div><span data-testid="myJobsStateDate">Posted 2 days ago</span></div>')
    job_date = getjobdate(job_element)
    assert job_date == ["Posted 2 days ago"]

def test_get_job_date_valid_date():
    """Test getting a valid date"""
    # Test case for a valid job posting date
    job_element = create_mock_job_element(date="2024-03-18")
    assert getjobdate(job_element) == ["2024-03-18"]


def test_get_job_date_active_days_ago():
    """Test getting active date"""
    # Test case for job posted 'X days ago' format
    job_element = create_mock_job_element(date="Active 2 days ago")
    assert getjobdate(job_element) == ['Active 2 days ago']


def test_get_job_date_exception():
    """Test failure of job date"""
    # Test case when extraction fails
    job_element = etree.fromstring('<div></div>')
    job_date = getjobdate(job_element)
    assert job_date == []

def test_getjobdate_without_date():
    """Test job date not available"""
    # Example lxml element without job posting date
    job_without_date = """
    <div>
        <!-- No date available -->
    </div>
    """

    expected_date = "Not available"
    assert getjobdate(job_without_date) == expected_date

# Helper function to create a mock job element
def create_mock_job_element(date):
    """Create mock job element"""
    # pylint: disable=too-few-public-methods
    class MockJobElement:
        """Mock job element class"""
        def __init__(self, date):
            # Mocking the xpath method to return the date
            self.xpath = lambda x: [date]

    return MockJobElement(date)

# Test formatJobDate function

def test_format_job_date_just_posted():
    """Test format job date just posted"""
    # Test case for formatting job date "Just posted"
    job_date_string = ["Just posted"]
    formatted_date = formatjobdate(job_date_string)
    assert formatted_date == datetime.now().strftime("%Y-%m-%d")


def test_format_job_date_today():
    """Format job date today"""
    # Test case for formatting job date "Today"
    job_date_string = ["Today"]
    formatted_date = formatjobdate(job_date_string)
    assert formatted_date == datetime.now().strftime("%Y-%m-%d")


def test_format_job_date_other_format():
    """Format job date other"""
    # Test case for formatting other date formats
    job_date_string = ["Active 5 days ago"]
    formatted_date = formatjobdate(job_date_string)
    assert formatted_date == "Active 5 days ago"
