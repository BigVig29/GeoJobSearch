"""Statements to import modules"""
import json
import os
from unittest.mock import patch, MagicMock
import pytest
from job_processor import (
    update_file_with_json_array,
    append_json_array_from_source_to_target,
    remove_header_tags,
    parse_location,
    extract_salary,
    preprocess_jobs,
    main_job_processor
)
# Test the update_file_with_json_array function

@pytest.fixture(name="json_input_file")
def json_input_file_fixture(tmp_path):
    """Fixture for a file path"""
    # Create a temporary JSON input file with some sample JSON objects
    input_file_path = os.path.join(tmp_path, "input.json")
    with open(input_file_path, 'w', encoding='utf-8') as file:
        file.write('{"title": "Job 1", "company": "Company A"}\n')
        file.write('{"title": "Job 2", "company": "Company B"}\n')
        file.write('{"title": "Job 3", "company": "Company C"}\n')
    return input_file_path

def test_update_file_with_json_array(json_input_file):
    """Test updating file"""
    # Call the function with the temporary input file
    output_file_path = update_file_with_json_array(json_input_file)

    # Assert that the output file exists
    assert os.path.exists(output_file_path)

    # Read the content of the output file and parse it as JSON
    with open(output_file_path, 'r', encoding='utf-8') as outfile:
        output_data = json.load(outfile)

    # Assert that the output data is a list of dictionaries
    assert isinstance(output_data, list)
    assert len(output_data) == 3
    assert output_data[0]["title"] == "Job 1"
    assert output_data[1]["company"] == "Company B"
    assert output_data[2]["title"] == "Job 3"


# Test the append_json_array_from_source_to_target function
@pytest.fixture(name="source_and_target_files")
def source_and_target_files_fixture(tmp_path):
    """Fixture for files"""
    # Create temporary source and target JSON files with sample data
    source_file_path = os.path.join(tmp_path, "source.json")
    target_file_path = os.path.join(tmp_path, "target.json")

    # Sample source data
    source_data = [{"title": "Job 1", "company": "Company A"},
                   {"title": "Job 2", "company": "Company B"}]
    with open(source_file_path, 'w', encoding='utf-8') as source_file:
        json.dump(source_data, source_file, indent=4)

    # Sample target data
    target_data = [{"title": "Existing Job 1", "company": "Existing Company"}]
    with open(target_file_path, 'w', encoding='utf-8') as target_file:
        json.dump(target_data, target_file, indent=4)

    return source_file_path, target_file_path


def test_append_json_array_from_source_to_target(source_and_target_files):
    """Test to add array from source to target file"""
    # Unpack source and target file paths
    source_file, target_file = source_and_target_files

    # Call the function
    append_json_array_from_source_to_target(source_file, target_file)

    # Assert that the target file has been updated with the source data
    with open(target_file, 'r', encoding='utf-8') as target_file:
        updated_target_data = json.load(target_file)

    assert len(updated_target_data) == 3
    assert updated_target_data[0]["title"] == "Existing Job 1"
    assert updated_target_data[1]["title"] == "Job 1"
    assert updated_target_data[2]["company"] == "Company B"

# Test the remove_header_tags function


def test_remove_header_tags():
    """Test to remove header tags"""
    # Sample job description with HTML header tags
    description_with_headers = """
    <h1>This is a header</h1>
    <p>This is a paragraph.</p>
    <h2>This is another header</h2>
    <p>This is another paragraph.</p>
    """

    # Call the function
    cleaned_description = remove_header_tags(description_with_headers)

    # Assert that header tags are removed
    assert "<h1>" not in cleaned_description
    assert "<h2>" not in cleaned_description
    assert "<h3>" not in cleaned_description

# Test the parse_location function


def test_parse_location_remote():
    """Test to parse a remote location"""
    # Test case for "Remote"
    city, province = parse_location("Remote")
    assert city == "Remote"
    assert province == "Remote"


def test_parse_location_hybrid_remote():
    """Test to parse a hybrid remote location"""
    # Test case for "Hybrid remote in"
    city, province = parse_location("Hybrid remote in New York, NY")
    assert city == "New York"
    assert province == "NY"


def test_parse_location_remote_in():
    """Test to parse a remote in location"""
    # Test case for "Remote in"
    city, province = parse_location("Remote in Los Angeles, CA")
    assert city == "Los Angeles"
    assert province == "CA"


def test_parse_location_city_and_province():
    """Test to parse a city and province"""
    # Test case for city and province separated by comma
    city, province = parse_location("New York, NY")
    assert city == "New York"
    assert province == "NY"


def test_parse_location_city_only():
    """Test to parse a city only"""
    # Test case for city only
    city, province = parse_location("Chicago")
    assert city == "Chicago"
    assert province == "NA"

# Test the extract_salary function
def test_extract_salary_no_info():
    """Test to extract salary with info"""
    # Test case for no salary information
    salary = extract_salary(
        ["Experience with Python is required", "Full-time position"])
    assert salary is None


def test_extract_salary_insufficient_insights():
    """Test salary for insufficient insights"""
    # Test case for insufficient insights
    salary = extract_salary(["$100,000 a year"])
    assert salary is None

# Test the preprocess_jobs function

@pytest.fixture(name="sample_json_data")
def sample_json_data_fixture(tmp_path):
    """Fixture for sample data"""
    # Create a temporary JSON file with sample job listings
    sample_data = [
        {
            "title": "Job 1",
            "company": "Company A",
            "location": "New York, NY",
            "description": "This is a job description",
            "insights": ["Insight 1", "Insight 2", "Insight 3"]
        },
        {
            "title": "Job 2",
            "company": "Company B",
            "location": "Los Angeles, CA",
            "description": "This is another job description",
            "insights": ["Insight 1", "Insight 2", "Insight 3"]
        }
    ]
    json_file_path = tmp_path / "sample_jobs.json"
    with open(json_file_path, 'w', encoding="utf-8") as f:
        json.dump(sample_data, f)
    return str(json_file_path)


@patch("job_processor.extract_salary")
@patch("job_processor.remove_header_tags")
@patch("job_processor.parse_location")
def test_preprocess_jobs(parse_location_mock, remove_header_tags_mock, extract_salary_mock, sample_json_data):
    """Test preprocess jobs function"""
    # Mock dependencies
    parse_location_mock.return_value = ("New York", "NY")
    remove_header_tags_mock.side_effect = lambda x: x  # Pass through the input
    extract_salary_mock.return_value = 50000  # Mock salary value

    # Call the function
    result = preprocess_jobs(sample_json_data)

    # Assertions
    assert len(result) == 2
    assert result[0]["Title"] == "Job 1"
    assert result[0]["Company"] == "Company A"
    assert result[0]["Location"] == "New York, NY"
    assert result[0]["City"] == "New York"
    assert result[0]["Province"] == "NY"
    assert result[0]["Description"] == "This is a job description"
    assert result[0]["Salary"] == 50000
    # Assuming job type is the second element in insights
    assert result[0]["JobType"] == "Insight 2"


def test_preprocess_jobs_file_not_found():
    """Test preprocess jobs with no file"""
    # Test if the function raises FileNotFoundError for a non-existent file
    with pytest.raises(FileNotFoundError):
        preprocess_jobs("non_existent_file.json")

# Test the main_job_processor function


@pytest.fixture(name="json_file_path")
def json_file_path_fixture(tmp_path):
    """Fixture for file path"""
    # Create a temporary JSON file path
    json_file_path = tmp_path / "test_jobs.json"
    return str(json_file_path)


@patch("job_processor.preprocess_jobs")
@patch("job_processor.db_conn_util.connect_to_mysql")
@patch("job_processor.db_conn_util.insert_all_jobs")
def test_main_job_processor(insert_all_jobs_mock, connect_to_mysql_mock, preprocess_jobs_mock, json_file_path):
    """Test the main function"""
    # Mock preprocess_jobs to return sample data
    preprocess_jobs_mock.return_value = [{"title": "Job 1", "company": "Company A"}, {
        "title": "Job 2", "company": "Company B"}]

    # Mock connect_to_mysql to return a dummy connection
    connect_to_mysql_mock.return_value = MagicMock()

    # Call the function
    main_job_processor(json_file_path)

    # Assertions
    preprocess_jobs_mock.assert_called_once_with(json_file_path)
    connect_to_mysql_mock.assert_called_once()
    insert_all_jobs_mock.assert_called_once_with(
        [{"title": "Job 1", "company": "Company A"}, {"title": "Job 2", "company": "Company B"}])
