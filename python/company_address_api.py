"""Modules to import googlemaps"""
import googlemaps

gmaps = googlemaps.Client(key="AIzaSyCoaU_ogrITCwvlxIdKjMCPBQlFsK-37yM")


def geocode_address(address):
    """
    Geocodes an address using the Google Maps Geocoding API.

    Parameters:
    - address (str): The address to geocode.

    Returns:
    - A tuple containing the latitude, longitude, and formatted address of the geocoded location.
      Returns (None, None, None) if the geocoding fails or no result is found.

    Raises:
    - Exception: If any errors occur during the geocoding process.
    """
    try:
        print(f"==INFO[geocode_address]== Geocoding {address} ")
        geocode_result = gmaps.geocode(address)

        if not geocode_result:
            raise IndexError("No results found.")
        return gecode_calc(geocode_result)
    except Exception as e:
        print(f"An error occurred during geocoding: {e}")
        raise

def gecode_calc(geocode_result):
    """Calculation of geocode"""
    if geocode_result:
        geometry = geocode_result[0]['geometry']
        lat = geometry['location']['lat']
        lng = geometry['location']['lng']
        formatted_address = geocode_result[0]['formatted_address']
        return lat, lng, formatted_address
    print("No results found.")
    return None, None, None
