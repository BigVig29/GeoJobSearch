import React, { useState, useEffect} from 'react';
import PropTypes from 'prop-types'
import axios from 'axios';
import Select from 'react-select';




const Sort = ({OnApplySort, currLocation, currJobType, currMinSalary, currMaxSalary, searchQuery}) =>{


    const[sortBy, setSortBy] = useState({value:"default", label:"Default"});

    const options = [
        
        {value:'default', label: 'Default' },

        {value:'date', label:'Date(Most Recent)' },

        {value:'salary', label:'Salary (High to low)' }
    ];
    

   


    useEffect(() =>{

            axios.get('/api/jobs/sort', { params: {

                location:currLocation,
                jobType:currJobType,
                minSalary:currMinSalary,
                maxSalary:currMaxSalary,
                search:searchQuery,
                sortBy:sortBy.value

            }})
                .then(response => {

                    const sortedJobs = response.data
                    OnApplySort(sortedJobs)
                })
                .catch(error => {
                    console.error('Error fetching sorted jobs:', error);
                });

        
        

        },[currLocation,currJobType,currMinSalary,currMaxSalary,searchQuery,sortBy,OnApplySort])
          


    return(

        <div className="sort-field"> 
                <label className="sort-label">
                    Sort By:
                </label>
            <div className ="sort-dropdown">

                <Select
                        onChange={(sortBy) => setSortBy(sortBy)}
                        value = {sortBy}
                        options = {options}
                        defaultValue={options[0]}
                        isSearchable = {false}
                />
            </div>
         </div>





    );

}; 


export default Sort;


Sort.propTypes = {

    OnApplySort: PropTypes.func.isRequired,
    searchQuery:PropTypes.string,
    currJobType:PropTypes.string,
    currLocation:PropTypes.string,
    currMinSalary:PropTypes.number,
    currMaxSalary:PropTypes.number

}
