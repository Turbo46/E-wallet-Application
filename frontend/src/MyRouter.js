import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import App from "./App";
import Login from "./Login/Login";
import Registration from "./Registration/Registration";
import Dashboard from "./Dashboard/Dashboard";

function MyRouter() {
    return(
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login />}></Route>
                <Route path="/registration" element={<Registration />}></Route>
                <Route path="/dashboard" element={<Dashboard />}></Route>
            </Routes>
        </BrowserRouter>
    )
}

export default MyRouter;