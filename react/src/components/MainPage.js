import React, { useCallback, useState } from 'react';
import MainJobListContainer from './JobListMain';
import SearchBar from './Search';
import Filters from './Filters';
import CoordinatePlotter from './CoordinatePlotter';
import './styles.css';
import Sort from './Sort';
import Coordinates from './Coordinates';

// States for Map or Job List view
const VIEWS = {
    JOB_LIST: 'JOB_LIST',
    MAP: 'MAP',
};


const PageContainer = () => {
    const [filteredJobs, setFilteredJobs] = useState(null);
    const [searchedJobs, setSearchedJobs] = useState(null);
    const [keyword, setKeyword] = useState(null);
    const [activeView, setActiveView] = useState(VIEWS.JOB_LIST); 
    const [sortedJobs, setSortedJobs] = useState(null);

    const[location,setLocation] = useState(null)
    const[jobType,setJobType] = useState(null)
    const[minSalary,setMinSalary] = useState(null)
    const[maxSalary,setMaxSalary] = useState(null)

    const[jobCoordinates, setJobCoordinates] = useState(null);


    //temporary place holder for searching features
    const handleSearch = useCallback ((jobs) => {
        setSearchedJobs(jobs);
    },[]);

    const handleFilters = useCallback((jobs) => {
        setFilteredJobs(jobs);
    },[]);

    const handleSort = useCallback((jobs) => {
        setSortedJobs(jobs);
    },[]);

    const sendCoordinates = useCallback((jobs) => {

        setJobCoordinates(jobs);

    },[])


    const getFiltersParams = useCallback((location,jobType, minSalary,maxSalary) => {

        setLocation(location);
        setJobType(jobType);
        setMinSalary(minSalary);
        setMaxSalary(maxSalary);


    },[]);

    return (
    //Parent container for all compomnents*
        <div className="page-container">
            <div className="header">
                <h2 className="website-title">GeoJobSearch</h2>
            </div>

            {/*Render the search bar component */}
            <div className="search-container">
                <SearchBar onSearch={handleSearch} SetKeyword={setKeyword} Keyword={keyword} />
            </div>

            {/*Render the filters component */}
            <div className="filters-container">
                <Filters OnApplyFilter={handleFilters} searchString={keyword} GetFiltersParams={getFiltersParams} />
            </div>

            <div className="sort-container">

                <Sort OnApplySort = {handleSort} currLocation = {location} currJobType={jobType} currMinSalary ={minSalary} currMaxSalary={maxSalary} searchQuery = {keyword} />
            
            </div>

            <div className="button-container">
            {activeView === VIEWS.JOB_LIST ? (
                <button
                className="main-or-map-button"
                onClick={() => setActiveView(VIEWS.MAP)}
                >
                Show Map
                </button>
            ) : (
                <button
                className="main-or-map-button"
                onClick={() => setActiveView(VIEWS.JOB_LIST)}
                >
                Show Job List
                </button>
            )}
            </div>

            <div className="map-api">

                <Coordinates onFilterChange = {sendCoordinates} currLocation = {location} currJobType={jobType} currMinSalary ={minSalary} currMaxSalary={maxSalary} searchQuery = {keyword} />

            </div>

            {/*Flip between job list and map*/}
            {activeView === VIEWS.JOB_LIST && (
                <div className="main-container">
                    <MainJobListContainer FilteredJobs={filteredJobs} SearchedJobs={searchedJobs} Keyword={keyword} SortedJobs ={sortedJobs} />
                </div>
            )}

            {activeView === VIEWS.MAP && (
                // <div className="map-container" style={{ display: 'block' }}>
                <div className="map-container">
                    <CoordinatePlotter coordinates={jobCoordinates} />
                </div>
            )}

            
        </div>
    );
};

export default PageContainer;
