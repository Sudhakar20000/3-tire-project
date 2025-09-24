// Automatically pick host and port from the system
const { protocol, hostname, port } = window.location;

// If no port, use 8080 by default (optional fallback)
const dynamicPort = port || '8080';

// Build base URLs dynamically
const BASE_URL_ = `http://10.0.0.82:9092/eidiko-workorder-integration-service`;


// const BASE_URL_ = `${protocol}//${hostname}:${dynamicPort}/eidiko-workorder-integration-service`;

export const API_ENDPOINTS = {
    login: `${BASE_URL_}/api/auth/login`,
    signUp: `${BASE_URL_}/api/auth/register`,
    addEmployee: `${BASE_URL_}/employee/addEmployee`,
    addNewProject: `${BASE_URL_}/addProject`,
    getallapidata: `${BASE_URL_}/employee/getAllEmployeesAndProjects`,
    getSOWStatus: `${BASE_URL_}/employee/getSowStatus`,
    getPoStatus: `${BASE_URL_}/employee/getWoOrPoStatus`,
    updateEmployeeOrProject: `${BASE_URL_}/employee/updateEmployeeOrProject`,
    deleteEmployee: `${BASE_URL_}/employee/deleteEmployeeOrProject`,
    sendReport: `${BASE_URL_}/sendReportByMashreqReportingManager`,
    searchData: `${BASE_URL_}/Search?`,
    sendMail: `${BASE_URL_}/sendMail?`,
    addManager: `${BASE_URL_}/addManager`,
    getMashreqReportingManagers: `${BASE_URL_}/getAllManagers`,
    deleteManager: `${BASE_URL_}/deleteManager`,
};

