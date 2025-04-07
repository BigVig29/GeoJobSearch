import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BsFillPlusCircleFill } from 'react-icons/bs'; // Example icon from React Icons

const JobList = () => {
    const [jobs, setJobs] = useState([]);

    useEffect(() => {
        axios.get('/api/jobs')
            .then(response => {
                setJobs(response.data);
            })
            .catch(error => {
                console.error('Error fetching jobs:', error);
            });
    }, []);

    return (
        <div className="container mt-5"> {/* Add mt-5 class for margin-top */}
            <h2 className="text-center mt-1 mb-4">Job listings</h2>
            <div className="row">
                {jobs.map(job => (
                    <div className="col-md-4 mb-4" key={job.jobID}>
                        <div className="card">
                            <div className="card-body">
                                <h5 className="card-title">{job.title}</h5>
                                <h6 className="card-subtitle mb-2 text-muted">{job.company}</h6>
                                <div dangerouslySetInnerHTML={{ __html: job.description }} />
                                <p className="card-text">Salary: ${job.salary}</p>
                                <a href={job.link} className="card-link">Learn More</a>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
            <div className="text-center">
                <BsFillPlusCircleFill size={30} className="text-primary" />
                <p className="mt-2">Add New Job</p>
            </div>
        </div>
    );
};

export default JobList;
