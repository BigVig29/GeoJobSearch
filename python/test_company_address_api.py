"""Modules to import pytest, unittest mock and the functions testsed"""
from unittest.mock import MagicMock
import pytest
from company_address_api import geocode_address, gecode_calc

# Mocking a geocode result for testing purposes
geocode_result = [
    {
        "geometry": {
            "location": {
                "lat": 40.7128,
                "lng": -74.0060
            }
        },
        "formatted_address": "New York, NY, USA"
    }
]

def test_gecode_calc_with_result():
    """Test geocode calc with result"""
    # Test the function with a valid geocode result
    lat, lng, formatted_address = gecode_calc(geocode_result)
    assert lat == 40.7128
    assert lng == -74.0060
    assert formatted_address == "New York, NY, USA"

def test_gecode_calc_with_empty_result():
    """Test geocode calc with empty result"""
    # Test the function with an empty geocode result
    lat, lng, formatted_address = gecode_calc([])
    assert lat is None
    assert lng is None
    assert formatted_address is None

def test_gecode_calc_with_none_result():
    """test geocodecalc with no result"""
    # Test the function with None as geocode result
    lat, lng, formatted_address = gecode_calc(None)
    assert lat is None
    assert lng is None
    assert formatted_address is None

def test_gecode_calc_with_no_result_message(capsys):
    """Test geocodecalc with no result"""
    # Test the function when no results are found
    gecode_calc([])
    captured = capsys.readouterr()
    assert "No results found." in captured.out

# Mocking the googlemaps.Client
# pylint: disable=too-few-public-methods
class MockGoogleMapsClient:
    """Class to mock client"""
    def geocode(self):
        """function to mock client"""
        return [{'geometry': {'location': {'lat': 40.7128, 'lng': -74.0060},
                 'formatted_address': 'New York, NY, USA'}}]

# Patching googlemaps.Client with the mocked class
@pytest.fixture(name="mock_googlemaps_client")
def mock_googlemaps_client_fixture(monkeypatch):
    """Mock googlemaps client"""
    mock_client = MockGoogleMapsClient()
    monkeypatch.setattr('company_address_api.googlemaps.Client', MagicMock(return_value=mock_client))
    return mock_client

# Test cases for geocode_address function
def test_geocode_address_with_results(capsys):
    """Test geocodeadd with result"""
    address = "New York, NY, USA"
    geocode_address(address)
    captured = capsys.readouterr()
    assert "Geocoding New York, NY, USA" in captured.out

def test_geocode_address_without_results():
    """Test geocodeadd with no result"""
    address = "Invalid Address"
    with pytest.raises(Exception):
        geocode_address(address)
