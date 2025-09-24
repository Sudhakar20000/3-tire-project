import React from 'react';
import { useLocation } from 'react-router-dom';
import Navbar from '../pages/Navbar.js';

const Layout = ({ children }) => {
    const location = useLocation();
    const username = localStorage.getItem('username');

    const noHeaderRoutes = ['/', '/eidiko_workorder/', '/eidiko_workorder/signUp'];

    const is404 = location.pathname !== '/' &&
                  location.pathname !== '/eidiko_workorder/' &&
                  location.pathname !== '/eidiko_workorder/signUp' &&
                  location.pathname !== '/eidiko_workorder/dashboard' &&
                  !location.pathname.startsWith('/eidiko_workorder');

    const shouldShowHeader = location.pathname === '/eidiko_workorder/dashboard';

    return (
        <>
            {shouldShowHeader && <Navbar username={username} />}
            <main>{children}</main>
        </>
    );
};

export default Layout;
