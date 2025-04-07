
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Select, { components } from 'react-select';
import PropTypes from 'prop-types';


const Filters = ({OnApplyFilter, searchString, GetFiltersParams}) => {

    
    const [locations, setLocations] = useState([]);
    const[selectedLocation, setSelectedLocation] = useState(null);


    const[jobTypes, setJobTypes ] = useState ([]);
    const[selectedJobType,setSelectedJobType] = useState(null);

    const[selectedSalary, setSelectedSalary] = useState(null);
    const[selectedMinSalary, setSelectedMinSalary] = useState(null);
    const[selectedMaxSalary, setSelectedMaxSalary] = useState(null);

    const[locationValue,setLocationValue] = useState(null);
    const[jobTypeValue,setJobTypeValue] = useState(null);


    const[salaryRanges,setSalaryRanges] = useState([]);
    const[searchQuery, setSearchQuery] = useState(null);



    useEffect(()=>{

        setSearchQuery(searchString);
        setSelectedLocation(null);
        setSelectedSalary(null);
        setSelectedJobType(null);
        
    },[searchString])

    

    //retrieve list of locations from api when a new jobtype, and salary is selcted
    useEffect(() => {

        axios.get('/api/jobs/locations/search',{params:{

            jobType:jobTypeValue,
            minSalary:selectedMinSalary,
            maxSalary:selectedMaxSalary, 
            search: searchQuery,

        } })

            .then(response =>{

                const locationOptions = response.data.map(fullLocation => ({

                    value: fullLocation.location,
                    label: `${fullLocation.location} (${fullLocation.count}) `  

                }));

                setLocations(locationOptions);
                   
            })

            .catch(error => {

                console.error('Error retreiving locations:', error);

            });

    }, [jobTypeValue,selectedMinSalary,selectedMaxSalary,searchQuery]);



    //retrieve list of jobtypes from api when a new location, and salary is selected
    useEffect(() => {


        axios.get('/api/jobs/job-types/search', {params: {

            location:locationValue,
            minSalary:selectedMinSalary, 
            maxSalary:selectedMaxSalary,
            search:searchQuery,

        }
    
        })
            .then(response =>{

                const jobTypeOptions = response.data.map(jobTypeCount => ({

                    value: jobTypeCount.jobType,
                    label: `${jobTypeCount.jobType} (${jobTypeCount.count}) `  

                }));

                setJobTypes(jobTypeOptions);
                
            })

            .catch(error => {

                console.error('Error retreiving job types', error);

            });

    }, [locationValue,selectedMinSalary,selectedMaxSalary,searchQuery]);


    
    //retrieve list of salaries from api when a new job type , and location is selected
    useEffect(() => {

        axios.get('/api/jobs/salary/search', {params: {

            location:locationValue,
            jobType:jobTypeValue,
            search:searchQuery,

        }
    
        })
            .then(response =>{

                const salaryOptions = response.data.map(salaryOptionsCount => ({

                    minSalary: salaryOptionsCount.minSalary,
                    maxSalary: salaryOptionsCount.maxSalary,
                    count:salaryOptionsCount.count,
                    //format the location values
                    label:`$${salaryOptionsCount.minSalary.toLocaleString()} - $${salaryOptionsCount.maxSalary.toLocaleString()} (${salaryOptionsCount.count})`,
                    value:`$${salaryOptionsCount.minSalary.toLocaleString()} - $${salaryOptionsCount.maxSalary.toLocaleString()}`                        

                }));

                setSalaryRanges(salaryOptions);
                
            })

            .catch(error => {

                console.error('Error retreiving salaries', error);

            });

    }, [locationValue,jobTypeValue,searchQuery]);





    //update selected location value
    useEffect(() =>{

        if(selectedLocation){

            setLocationValue(selectedLocation.value);
        }else{

            setLocationValue(null);
        }
      

    },[selectedLocation]);
     

    
    

    //update selected job type value
    useEffect(() =>{

        if(selectedJobType){

            setJobTypeValue(selectedJobType.value);
        } else{

            setJobTypeValue(null);            
        }   

    },[selectedJobType]);


    //updates the values of minimum and maximum salary when a new range is selected
    useEffect(() =>{

        if(selectedSalary){

            setSelectedMinSalary(selectedSalary.minSalary);
            setSelectedMaxSalary(selectedSalary.maxSalary);

            

        } else{

            setSelectedMinSalary(null);
            setSelectedMaxSalary(null);

            
        }   

    },[selectedSalary]);
         



    //retrieve jobs based on filters when a new filtering criteria is selected or updated
    useEffect(() => {


        GetFiltersParams(locationValue,jobTypeValue,selectedMinSalary,selectedMaxSalary);

        axios.get('/api/jobs/filter/search', {params: {

            location:locationValue,
            jobType:jobTypeValue,
            minSalary:selectedMinSalary,
            maxSalary:selectedMaxSalary,
            search:searchQuery

        }})

            .then(response =>{

                //handle response
                const filteredJobs = response.data;
                OnApplyFilter(filteredJobs);
                    

            })

            .catch(error => {
                console.error('Error sending filter information:', error);
            });

    }, [OnApplyFilter,locationValue, jobTypeValue,selectedMaxSalary,selectedMinSalary,searchQuery, GetFiltersParams]);


    return(
        //render location list dropdown
        <div className="filters-bar" >
            <div className="location-dropdown"> 
                <Select

                    onChange={(selectedLocation) => setSelectedLocation(selectedLocation)}
                    value = {selectedLocation}
                    options = {locations}
                    components={{SingleValue: selectedOptionDisplay}}
                    placeholder = "Location"
                    isSearchable = {true}
                    isClearable = {true}
            

                />
            </div>

            <div className="job-type-dropdown">

                <Select

                    onChange={(selectedJobType) => setSelectedJobType(selectedJobType) }
                    options = {jobTypes}
                    value = {selectedJobType}
                    components={{SingleValue: selectedOptionDisplay}}
                    placeholder = "Job Type"
                    isSearchable = {true}
                    isClearable = {true}

                />

            </div>

            <div className= "salary-field">

                <Select

                    onChange={(selectedSalary) => setSelectedSalary(selectedSalary)}
                    options = {salaryRanges}
                    value = {selectedSalary}
                    components={{SingleValue: selectedOptionDisplay}}
                    placeholder = "Salary"
                    isSearchable = {true}
                    isClearable = {true}
                
                />

            </div>

        </div>
   
    );

};

Filters.propTypes = {

    OnApplyFilter: PropTypes.func.isRequired,
    GetFiltersParams: PropTypes.func.isRequired,
    searchString: PropTypes.string  
};



//Component to overide what is displayed in the input field of the dropdown
const selectedOptionDisplay = ({children, ...props}) => {

    const selectedOption = props.data;
    return(

        <components.SingleValue {...props}>
            {/*Render the value of the selected option*/}
            <span>{selectedOption.value}</span>

        </components.SingleValue>    
    );
};


selectedOptionDisplay.propTypes ={

    data:PropTypes.shape({

        value:PropTypes.any.isRequired

    })

};




export default Filters;
