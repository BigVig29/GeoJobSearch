import {useEffect } from 'react';
import axios from 'axios';

import PropTypes from 'prop-types';

const Coordinates = ({ onFilterChange, currLocation, currJobType, currMinSalary, currMaxSalary, searchQuery }) => {

    useEffect(() => {

        axios.get('http://localhost:8080/api/jobs/coordinates', {params: {

                location:currLocation,
                jobType:currJobType,
                minSalary:currMinSalary,
                maxSalary:currMaxSalary,
                search:searchQuery,

    }})

        .then(response =>{

            //handle response
            const jobsWithCoordinates = response.data;
            onFilterChange(jobsWithCoordinates);
                
        })

        .catch(error => {
            console.error('Error sending coordinates information:', error);
        });

    }, [onFilterChange, currLocation, currJobType, currMinSalary, currMaxSalary, searchQuery]);


    
};

export default Coordinates;


Coordinates.propTypes = {

    onFilterChange:PropTypes.func.isRequired,
    searchQuery:PropTypes.string,
    currJobType:PropTypes.string,
    currLocation:PropTypes.string,
    currMinSalary:PropTypes.number,
    currMaxSalary:PropTypes.number

}


