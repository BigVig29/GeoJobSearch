"""Modules to import unittest mock, pytest and functions tested"""
from unittest.mock import MagicMock, patch
import pytest
from db_conn_util import connect_to_mysql, get_geocoded_address


# Test connect_to_mysql
@pytest.fixture(name="mysql_connect_mock")
def mysql_connect_mock_fixture():
    """Create mock connection"""
    # Mocking mysql.connector.connect
    with patch('mysql.connector.connect') as connect_mock:
        yield connect_mock


def test_connect_to_mysql_success(mysql_connect_mock):
    """Test sql connection"""
    # Mock a successful connection
    connection_mock = MagicMock()
    mysql_connect_mock.return_value = connection_mock

    # Call the function
    connection = connect_to_mysql()

    # Assertions
    assert connection == connection_mock
    mysql_connect_mock.assert_called_once_with(
        host='algorise-mysql',
        user='root',
        password='0000',
        database='GeoJobSearch'
    )

@pytest.fixture(name="""mock_geocode_address""")
def mock_geocode_address_fixture():
    """Mock geocoding of address"""
    with patch("db_conn_util.geocode_address") as mocked_geocode_address:
        # Mocking the return value of geocode_address
        mocked_geocode_address.return_value = (123.456, -45.678, "Mocked Address")
        yield mocked_geocode_address

def test_get_geocoded_address(mock_geocode_address):
    """Test geocoding of address"""
    company_name = "Mock Company"
    city = "Mock City"
    province = "Mock Province"

    # Call the function
    result = get_geocoded_address(company_name, city, province)

    # Assert geocode_address is called with the correct address
    mock_geocode_address.assert_called_once_with(f"{company_name}, {city}, {province}")

    # Assert the return value is handled correctly
    assert result == (123.456, -45.678, "Mocked Address")
