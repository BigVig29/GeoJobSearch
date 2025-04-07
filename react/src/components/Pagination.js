import React from 'react';
import PropTypes from 'prop-types'; // Import PropTypes
import './styles.css';

const Pagination = ({ nPages, currPage, setCurrPage }) => {
    // Always show the first page, current page, last page, and 5 pages around the current page
    let startPage, endPage;
    if (nPages <= 9) {
        // Show all pages if there are 9 or fewer
        startPage = 1;
        endPage = nPages;
    } else {
        // More than 9 pages
        if (currPage <= 5) {
            startPage = 1;
            endPage = 5;
        } else if (currPage + 4 >= nPages) {
            startPage = nPages - 4;
            endPage = nPages;
        } else {
            startPage = currPage - 1;
            endPage = currPage + 1;
        }
    }

    const nextPage = () => {
        if (currPage < nPages) {
            setCurrPage(currPage + 1);
        }
    };

    const prevPage = () => {
        if (currPage > 1) {
            setCurrPage(currPage - 1);
        }
    };

 

    return (
        <nav>
            <ul className='pagination justify-content-center'>
                <li className="page-item">
                    <button onClick={prevPage} className='page-link'>Previous</button>
                </li>
                {startPage > 1 && (
                    <>
                        <li className="page-item">
                            <button onClick={() => setCurrPage(1)} className="page-link">1</button>
                        </li>
                        {startPage > 2 && (
                            <li className="page-item disabled">
                                <span className="page-link">...</span>
                            </li>
                        )}
                    </>
                )}
                {Array.from({ length: endPage - startPage + 1 }, (_, idx) => startPage + idx).map(pageNumber => (
                    <li key={pageNumber} className={`page-item ${currPage === pageNumber ? 'active' : ''}`}>
                        <button onClick={() => setCurrPage(pageNumber)} className="page-link">{pageNumber}</button>
                    </li>
                ))}
                {endPage < nPages && (
                    <>
                        {endPage < nPages - 1 && (
                            <li className="page-item disabled">
                                <span className="page-link">...</span>
                            </li>
                        )}
                        <li className="page-item">
                            <button onClick={() => setCurrPage(nPages)} className="page-link">{nPages}</button>
                        </li>
                    </>
                )}
                <li className="page-item">
                    <button onClick={nextPage} className='page-link'>Next</button>
                </li>
            </ul>
        </nav>
    );
};

// Add prop type validation
Pagination.propTypes = {
    nPages: PropTypes.number.isRequired,
    currPage: PropTypes.number.isRequired,
    setCurrPage: PropTypes.func.isRequired
};



export default Pagination