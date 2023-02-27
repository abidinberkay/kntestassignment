import React, {useState, useEffect} from "react";
import Table from 'react-bootstrap/Table';
import Pagination from 'react-bootstrap/Pagination';
import 'bootstrap/dist/css/bootstrap.min.css';
import * as cityService from "../service/CityService";
import Button from "react-bootstrap/Button";
import {useNavigate} from "react-router-dom";
import '../style/CityTable.css';
import {Card} from "react-bootstrap";

function CityTable() {
    const [selectedPageNumber, setSelectedPageNumber] = useState(1);
    const [perPage, setPerPage] = useState(5);
    const [totalElement, setTotalElement] = useState(0);
    const [currentData, setCurrentData] = useState([]);
    const [pageNavigationNumber, setPageNavigationNumber] = useState(1);
    const [cityName, setCityName] = useState('');

    const totalPages = Math.ceil(totalElement / perPage);
    let navigate = useNavigate();


    useEffect(() => {
        findCities(cityName);
    }, [selectedPageNumber, perPage]);

    const changeSelectedPageNumber = (pageNumber) => {

        setPageNavigationNumber(pageNumber);
        setSelectedPageNumber(pageNumber);
    };

    const changePageSize = (event) => {
        const newPerPage = parseInt(event.target.value, 10);
        setSelectedPageNumber(1);
        setPerPage(newPerPage);
    };

    const findCities = async (cityName) => {
        let result = await cityService.getCityPage(cityName, selectedPageNumber - 1, perPage, sessionStorage.getItem('jwtToken'));
        setCurrentData(result.data.content);
        setTotalElement(result.data.totalElements);
    };

    const handleRowClick = (item) => {
        sessionStorage.setItem('id', item.id);
        sessionStorage.setItem('cityname', item.name);
        sessionStorage.setItem('photo', item.photo);
        navigate("/details")

    }

    const addDefaultSrc = (ev) => {
        ev.target.src = require('../assets/nophoto.png');
    }

    return (
        <div className="city-table-container">
            <Card className="search-panel">
                <input type={"search"}
                       placeholder={"Search City"}
                       onChange={e => setCityName(e.target.value)}
                       className="search-input input-box"
                ></input>
                <Button className="lrbutton search-button" onClick={() => findCities(cityName)}>Search</Button>
            </Card>
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Photo</th>
                </tr>
                </thead>
                <tbody>
                {currentData.map((city) => (
                    <tr key={city.id} onClick={() => handleRowClick(city)}>
                        <td>{city.id}</td>
                        <td>{city.name}</td>
                        <td><img onError={addDefaultSrc} width={50} src={city.photo}/></td>
                    </tr>
                ))}
                </tbody>
            </Table>
            <div className="d-flex justify-content-between align-items-center">
                <Pagination className="pagination">
                    <select value={perPage} onChange={changePageSize}>
                        <option value="5">5</option>
                        <option value="10">10</option>
                        <option value="15">15</option>
                    </select>
                    <Pagination.Prev
                        onClick={() => changeSelectedPageNumber(selectedPageNumber - 1)}
                        disabled={selectedPageNumber === 1}
                    />
                    {Array.from({length: totalPages + 1}, (_, i) => i)
                        .slice(selectedPageNumber <= 5 ? 1 : selectedPageNumber - 5, selectedPageNumber < 5 ? 10 : selectedPageNumber + 6)
                        .map(page => (
                            <Pagination.Item
                                key={page}
                                active={page === selectedPageNumber}
                                onClick={() => changeSelectedPageNumber(page)}
                            >
                                {page}
                            </Pagination.Item>
                        ))
                    }
                    <Pagination.Next
                        onClick={() => changeSelectedPageNumber(selectedPageNumber + 1)}
                        disabled={selectedPageNumber === totalPages}
                    />
                </Pagination>
            </div>
            <div>
                <Card className="gotopage-panel">
                    {/*Page number:*/}
                    <input type={"number"}
                           placeholder={"Go to page"}
                           value={pageNavigationNumber}
                           className="gotopage-button input-box"
                           onChange={e => setPageNavigationNumber(+e.target.value)}></input>
                    <Button className="lrbutton search-button gotopage-button"
                            onClick={() => changeSelectedPageNumber(pageNavigationNumber)}>Go
                        to page</Button>
                </Card>
            </div>
        </div>
    );
}

export default CityTable;
