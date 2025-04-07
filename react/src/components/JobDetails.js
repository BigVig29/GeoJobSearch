import React  from 'react';
import PropTypes from 'prop-types'



const JobDetails = ({job}) => {


    if(!job){

        return <div className='job-details-empty'>Select a job to see the details</div>

    }

    const formattedDate = new Date(job.date).toLocaleDateString("en-CA")

    // The content that displays when a job is selected
    return (
        <div className="job-details-container">
            <h2 className ="job-title" > {job.title} </h2>
            <h3 className ="job-company" >{job.company}</h3>
            <p className ="job-location">{job.location}</p>
            <div className = "insights-container">
                <p className = "job-type">{job.jobType}</p>
                <p className ="job-date"> Posting Date: {formattedDate}</p>
                <p className = "job-salary" > Salary: ${job.salary.toLocaleString()}</p>
            </div>
            <button className="Apply-Button" disabled={!job.jobURL} onClick={() => window.open(job.jobURL, '_blank', 'noopener,noreferrer') }>
                Apply
            </button>
            <div className="job-description" dangerouslySetInnerHTML={{ __html: job.description }}></div>
            
        </div>
    );
};


JobDetails.propTypes = {
    job: PropTypes.shape({
      title: PropTypes.string.isRequired,
      company: PropTypes.string.isRequired,
      location: PropTypes.string,
      jobType: PropTypes.string,
      description: PropTypes.string,
      salary: PropTypes.number,
      jobURL: PropTypes.string,
      date: PropTypes.number,


    })

};

export default JobDetails;
