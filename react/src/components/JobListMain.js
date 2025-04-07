import React, { useState, useEffect } from 'react';
import axios from 'axios';
import JobDetails from './JobDetails'
import JobList from './JobSideList'
import Pagination from './Pagination'
import './styles.css'
import PropTypes from 'prop-types';




const MainContainer = ({FilteredJobs, SearchedJobs, SortedJobs}) => {


    const [jobs,setJobs] = useState([])
    const[selectedJob,setSelectedJob] = useState(null)
    const[currentJobs,setCurrentJobs] = useState([])


     //page being shown in pagination
     const [currPage, setCurrPage] = useState(1);

     //how many jobs being shown on each page
     const jobsPerPage = 5;

     //index of last job on a certain page
     const indexOfLastJob = currPage * jobsPerPage;
    
     //index of first job on a certain page
     const indexOfFirstJob = indexOfLastJob - jobsPerPage;


     //calculation of the number of pages in total
     const nPages = Math.ceil(jobs.length / jobsPerPage);


    useEffect(() => {


        //jobs being shown on the page
        setCurrentJobs(jobs.slice(indexOfFirstJob, indexOfLastJob));

    },[jobs,indexOfFirstJob,indexOfLastJob]);


    const getAllJobs = () =>{

        axios.get('http://localhost:8080/api/jobs')
        .then(response => {
            setJobs(response.data);
            if (response.data.length > 0) {
                // Automatically select the first job in the list
                setSelectedJob(response.data[0]);
            }
        })

        .catch(error => {
            console.error('Error fetching jobs:', error);
        });

    };


    useEffect (() => {

        if(!FilteredJobs && !SearchedJobs && !SortedJobs){

                getAllJobs();
        }

    })



        //update job list to searched jobs if a search was applied
        useEffect(()=>{

            if(SearchedJobs){

                setJobs(SearchedJobs); //use set of filtered jobs if filters were applied
                if (SearchedJobs.length > 0) {
                    // Automatically select the first job in the list
                    setSelectedJob(SearchedJobs[0]);
                }
        
                //must reset to page 1, for new filtered joblist
                setCurrPage(1);
            }

         },[SearchedJobs])



        //update job list to filtered job list if filters are applied
        useEffect(()=>{

        if(FilteredJobs){

            setJobs(FilteredJobs); //use set of filtered jobs if filters were applied
            if (FilteredJobs.length > 0) {
                // Automatically select the first job in the list
                setSelectedJob(FilteredJobs[0]);
            }

            //must reset to page 1, for new filtered joblist
            setCurrPage(1);

        }
        
        }, [FilteredJobs]);// add filtered jobs dependency


        //update job list sort
        useEffect(()=>{

            if(SortedJobs){
    
                setJobs(SortedJobs); //use set of sorted jobs if filters were applied
                if (SortedJobs.length > 0) {
                    // Automatically select the first job in the list
                    setSelectedJob(SortedJobs[0]);
                }
    
                //must reset to page 1, for new filtered joblist
                setCurrPage(1);
    
            }
            
            }, [SortedJobs]);// add filtered jobs dependency
    

 
    //event handler to get the selected job
    const jobClicked = (jobID) => {

        const job = jobs.find(j => j.jobID === jobID);
        setSelectedJob(job);

    };


    return(
        <div>
            <div className ="main-container">
                {/*Side component for expanded view of Job listings */}
                <div className="job-side-view">

                <p className ="job-amount"> {jobs.length} result(s)</p>

                <JobList jobs = {currentJobs} onJobClick = {jobClicked} selectedJobID = {selectedJob ? selectedJob.jobID: null}/>

                {/* Pagination section */}
                <Pagination
                    nPages={nPages}
                    currPage={currPage}
                    setCurrPage={setCurrPage}
                />
                
                </div>


                {/*Main content area for job list */}
                <div className = "job-details">

                    <JobDetails job = {selectedJob} />
                </div>
            </div>


        </div>

    );
};

MainContainer.propTypes = {
    FilteredJobs:PropTypes.array,
    SearchedJobs:PropTypes.array,
    SortedJobs:PropTypes.array,              
};  


export default MainContainer