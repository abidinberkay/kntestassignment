import * as cityService from '../service/CityService';
import React, {useEffect, useState} from 'react';
import CityTable from "../component/CityTable";
import {Card} from "react-bootstrap";
import HeaderPanel from "../component/HeaderPanel";

function MainPage() {
    const [tableData, setTableData] = useState([]);
    const [totalElement, setTotalElement] = useState(0);

    const findCities = async (name, pageNumber, size) => {
        let result = await cityService.getCityPage(name, pageNumber, size, sessionStorage.getItem('jwtToken'), sessionStorage.getItem('jwtToken'));
        setTableData(result.data.content);
        setTotalElement(result.data.totalElements);
    }
    return (
        <>
            <div>
                <Card>
                    <div>
                        <HeaderPanel></HeaderPanel>
                    </div>
                    <CityTable params={tableData} totalElement={totalElement} findCities={findCities}></CityTable>
                </Card>
            </div>
        </>
    );
}

export default MainPage;
