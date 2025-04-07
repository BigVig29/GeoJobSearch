import React, { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import PropTypes from 'prop-types';
import L from 'leaflet';


// Helper component to adjust map view
const SetViewToBounds = ({ coordinates }) => {
    const map = useMap();

  

    useEffect(() => {

      //ignore remote jobs
      const validJobs = coordinates.filter(([,lat,lng]) => lat != null && lng != null);

      if (validJobs.length > 0) {
        
        const latLngs = validJobs.map(coord => L.latLng(coord[1], coord[2]));

        const bounds = L.latLngBounds(latLngs);
        map.fitBounds(bounds);
      }
    }, [coordinates, map]);
  
  
  
    return null;
};




const CoordinatePlotter = ({ coordinates }) => {
    // Set default position and zoom level for the map

    

    const defaultPosition = [50.000000, -85.000000]; //Center in ontario
    const zoomLevel = 13;


    return (

    <MapContainer id='map' center={defaultPosition} zoom={zoomLevel} >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />


    {coordinates.map((jobEntry) => {

      const job = jobEntry[0];
      const latitude = jobEntry[1];
      const longitude = jobEntry[2];

    
    if( latitude !== null && longitude !== null){

    return(
      
      <Marker key={job.jobID} position={[latitude,longitude]}>
             <Popup className="job-details">
              <h2 className ="job-title" > {job.title} </h2>
              <h3 className ="job-company" >{job.company}</h3>
              <p className ="job-location">{job.location}</p>
              <div className = "insights-container">
                  <p className = "job-type">{job.jobType}</p>
                  <p className ="job-date"> Posting Date: {new Date (job.date).toLocaleDateString("en-CA")}</p>
                  <p className = "job-salary" > Salary: ${job.salary.toLocaleString()}</p>
              </div>
              <button className="Apply-Button" disabled={!job.jobURL} onClick={() => window.open(job.jobURL, '_blank', 'noopener,noreferrer') }>
                  Apply
              </button>
              <div className="job-description" dangerouslySetInnerHTML={{ __html: job.description }}></div>
               
             </Popup>
           </Marker>
        );

    }else{

      return null;
    }



      })}

        {/* Dynamically adjust map view to include all markers */}
        <SetViewToBounds coordinates={coordinates} />
      </MapContainer>

    );

  };

SetViewToBounds.propTypes = {
coordinates: PropTypes.array.isRequired,        
};  

CoordinatePlotter.propTypes = {
  coordinates: PropTypes.array.isRequired,        
};  

export default CoordinatePlotter;
