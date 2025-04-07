// SearchBar.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';

import PropTypes from 'prop-types';

const SearchBar = ({ onSearch, SetKeyword, Keyword }) => {

    const [search, setSearch] = useState('');

    const handleSearch = ()=>{
        SetKeyword(search);
    };

    useEffect(() => {

        axios.get('/api/jobs/search', {params: {

        keyword: Keyword

    }})

        .then(response =>{

            //handle response
            const searchedJobs = response.data;
            onSearch(searchedJobs);
                

        })

        .catch(error => {
            console.error('Error sending filter information:', error);
        });

    }, [onSearch, Keyword]);


    return (
        <div className="search-bar-container">
            <input
                type="text"
                placeholder="Search jobs..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="search-input"
            />
            <button onClick={handleSearch} className="search-button">
                Search
            </button>
        </div>
    );
};

export default SearchBar;


SearchBar.propTypes = {


    onSearch:PropTypes.func.isRequired,
    SetKeyword:PropTypes.func.isRequired,
    Keyword:PropTypes.string,

}


