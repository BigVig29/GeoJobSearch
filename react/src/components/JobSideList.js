import React from 'react'
import PropTypes from 'prop-types'



const JobSideList = ({jobs,onJobClick,selectedJobID}) => {

    return (
        
        <div className="job-side-list">

            {jobs.map((job) => (

                <button key={job.jobID} className={`card mb-3 ${job.jobID === selectedJobID ? 'selected' : ''}`} 
                onClick={() => onJobClick(job.jobID)}>

                    <div className="card-body">
                        <h5 className="card-title">{job.title}</h5>
                        <h6 className="card-subtitle mb-2 text-muted">{job.company}</h6>
                        <div className="insigts-container-list">
                            <p className="job-salary">Salary: ${job.salary.toLocaleString()}</p>
                            <p className="job-date">Posting Date: {new Date(job.date).toLocaleDateString("en-CA")} </p>
                        </div>
                    </div>

                </button>

            ))}
        </div>
    );

};

export default JobSideList


//declare proptypes
JobSideList.propTypes = {

    jobs: PropTypes.arrayOf(
    
        PropTypes.shape({
            
        title: PropTypes.string.isRequired,
        company: PropTypes.string.isRequired,
        salary: PropTypes.number,

         })),

    selectedJobID:PropTypes.number,
    onJobClick:PropTypes.func.isRequired

}