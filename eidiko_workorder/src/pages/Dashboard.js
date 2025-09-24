import { Button, Grid2, IconButton, Typography, CircularProgress } from '@mui/material';
import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import CustomModal from '../components/CustomModal';
import CustomTable from '../components/CustomTable';
import { Employee_List_Columns as BaseColumns } from '../Constants/lable';
import BasicPie from '../components/Charts/reactchart';
import { useNavigate } from 'react-router-dom';
import UpdateEmp from './updateemp';
import AddEmp from './AddEmp';
import { TbEyeEdit, TbMapShare } from "react-icons/tb";
import { MdDeleteOutline } from "react-icons/md";
import { Snackbar, Alert } from '@mui/material';
import { API_ENDPOINTS } from '../api_properites/Api_URls';
import dayjs from "dayjs";
import { EmployeeContext } from '../contexts/EmployeeContext';
import AddNewProj from './AddNewProj';
import Dropdown from '../components/DropDown';
import AddNewManger from './AddNewManger';

function Dashboard() {
    const [open, setOpen] = useState(false);
    const [updateOpen, setUpdateOpen] = useState(false);
    const [rawDropdownData, setRawDropDownData] = useState([]);
    const [employeeData, setEmployeeData] = useState([]);
    const [selectedEmployee, setSelectedEmployee] = useState(null);
    const [sowData, setSowData] = useState([]);
    const [poData, setPoData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [chartLoading, setChartLoading] = useState(false);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [type, setType] = useState('');
    const [newProjOpen, setNewProjOpen] = useState(false);
    const [reportingManager, setReportingManager] = useState([]);
    const [selectedManager, setSelectedManager] = useState('');
    const [selectedEmployeeName, setSelectedEmployeeName] = useState('');
    const [selectedProject, setSelectedProject] = useState('');
    const [selectedStatus, setSelectedStatus] = useState('');
    const [employee, setEmployee] = useState([]);
    const [project, setProject] = useState([]);
    const [newselectedManager, setNewSelectedManager] = useState('');
    const [newselectedEmployee, setNewSelectedEmployee] = useState('');
    const [newselectedProject, setNewSelectedProject] = useState('');
    const [deleteConfirmOpen, setDeleteConfirmOpen] = useState(false);
    const [employeeToDelete, setEmployeeToDelete] = useState(null);
    const [handleManagerOpen, setHandleManagerOpen] = useState(false);

    console.log(employeeData, "employeeData123");


    const navigate = useNavigate();
    // console.log("selectedEmployee", selectedEmployee);


    const token = localStorage.getItem('token');

    const { refresh } = useContext(EmployeeContext);


    const axiosInstance = axios.create({
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    });

    const clearDropdowns = () => {
        setReportingManager([]);
        setEmployee([]);
        setProject([]);
    };
    const parseDate = (dateString) => {
        if (!dateString) return '';
        const parsed = new Date(dateString);
        return isNaN(parsed.getTime()) ? '' : parsed.toISOString().split('T')[0];
    };

    const fetchEmployees = async () => {
        setLoading(true);
        try {
            const response = await axiosInstance.get(API_ENDPOINTS.getallapidata);
            const rawData = response.data;

            const processedData = rawData.map((employee) => {
                return {
                    ...employee,
                    employeeId: employee.employeeId === 0 ? '' : employee.employeeId,
                    startDate: parseDate(employee.startDate),
                    contractStartDate: (!employee.employeeId && !employee.employeeName)
                        ? ''
                        : parseDate(employee.contractStartDate),
                    endDate: parseDate(employee.endDate),
                };
            });

            console.log("rawData", rawData);

            // console.log(processedData, "process data");

            setEmployeeData(processedData);
            setRawDropDownData(processedData);

            const uniqueManagers = [...new Set(rawData.map(emp => emp.mashreqReportingManager).filter(Boolean))];
            const uniqueEmployee = [...new Set(rawData.map(emp => emp.employeeName).filter(Boolean))];
            const uniqueProjects = [...new Set(rawData.map(proj => proj.projectName).filter(Boolean))];

            // setReportingManager([{ value: 'All', label: 'All' }, ...uniqueManagers.map(m => ({ value: m, label: m }))]);
            setEmployee([{ value: 'All', label: 'All' }, ...uniqueEmployee.map(e => ({ value: e, label: e }))]);
            setProject([{ value: 'All', label: 'All' }, ...uniqueProjects.map(p => ({ value: p, label: p }))]);

        } catch (error) {
            console.error('Error fetching employees:', error);
        } finally {
            setLoading(false);
        }
    };

    // const fetchManagers = async () => {
    //     try {
    //         const token = localStorage.getItem('token');
    //         const response = await axios.get(API_ENDPOINTS.getMashreqReportingManagers, {
    //             headers: {
    //                 Authorization: `Bearer ${token}`,
    //             },
    //         });

    //         const managers = response.data.result.managers;

    //         const formatted = managers
    //             .filter((m) => m.manager !== null)
    //             .map(m => ({ value: m.manager, label: m.manager }));

    //         setReportingManager(formatted);
    //     } catch (error) {
    //         console.error('Failed to fetch managers', error);
    //     }
    // };



    const filterData = async () => {
        if (
            !newselectedManager?.trim() &&
            !newselectedEmployee?.trim() &&
            !newselectedProject?.trim() &&
            !selectedStatus?.trim()
        ) {
            alert("Please select at least one field to filter the data.");
            return;
        }

        setLoading(true);

        try {
            const params = {};
            if (newselectedManager?.trim()) params.mashreqReportingManager = newselectedManager.trim();
            if (newselectedEmployee?.trim()) params.employeeName = newselectedEmployee.trim();
            if (newselectedProject?.trim()) params.projectName = newselectedProject.trim();
            if (selectedStatus?.trim()) params.status = selectedStatus.trim();

            const response = await axiosInstance.get(API_ENDPOINTS.searchData, { params });

            console.log("Full Axios Response:", response);

            const rawData = Array.isArray(response.data?.data)
                ? response.data.data
                : Array.isArray(response.data)
                    ? response.data
                    : [];

            console.log("rawData", rawData);

            const processedData = rawData.map((item) => ({
                ...item,
                employeeId: item.employeeId === 0 ? '' : item.employeeId || '',
                employeeName: item.employeeName || '',
                contractStartDate: (!item.employeeId && !item.employeeName)
                    ? ''
                    : parseDate(item.contractStartDate),
                startDate: parseDate(item.startDate),
                endDate: parseDate(item.endDate)
            }));
            console.log("rawData", rawData);

            console.log('processedData', processedData);


            if (processedData.length === 0) {
                alert("Data not found");
                setEmployeeData([]);
                clearDropdowns();
                fetchEmployees();
                return;
            }

            setEmployeeData(processedData);

            // const uniqueManagers = [...new Set(processedData.map(item => item.mashreqReportingManager).filter(Boolean))];
            const uniqueEmployees = [...new Set(processedData.map(item => item.employeeName).filter(Boolean))];
            const uniqueProjects = [...new Set(processedData.map(item => item.projectName).filter(Boolean))];

            // setReportingManager(uniqueManagers.map(m => ({ value: m, label: m })));
            setEmployee(uniqueEmployees.map(e => ({ value: e, label: e })));
            setProject(uniqueProjects.map(p => ({ value: p, label: p })));

        } catch (error) {
            if (error.response?.status === 404) {
                setEmployeeData([]);
                alert("Data not found");
                clearDropdowns();
                fetchEmployees();
            } else {
                console.error('Error fetching filtered data:', error);
                alert("Something went wrong while fetching data.");
            }
        } finally {
            setLoading(false);
        }
    };

    const sendReport = async () => {
        if (!newselectedManager && !newselectedEmployee && !newselectedProject && !selectedStatus) {
            alert('Please select at least one field');
            return;
        }

        try {
            const params = {};
            if (newselectedManager?.trim()) {
                params.mashreqReportingManager = newselectedManager.trim();
            }
            if (newselectedEmployee?.trim()) {
                params.employeeName = newselectedEmployee.trim();
            }
            if (newselectedProject?.trim()) {
                params.projectName = newselectedProject.trim();
            }
            if (selectedStatus?.trim()) {
                params.status = selectedStatus.trim();
            }


            if (selectedStatus?.trim()) {
                params.status = selectedStatus.trim();
            }

            // Pass params in URL using `params` option
            const response = await axiosInstance.post(
                API_ENDPOINTS.sendMail,
                {}, // Empty body
                { params } // Query parameters in URL
            );

            alert('Mail sent successfully!');
        } catch (error) {
            console.error('Error sending Mail:', error);
            alert('Failed to send Mail.');
        }
    };

    useEffect(() => {
        if (!rawDropdownData || !Array.isArray(rawDropdownData)) return;

        // Set Reporting Managers
        const uniqueManagers = [
            ...new Set(rawDropdownData.map(emp => emp.mashreqReportingManager).filter(Boolean))
        ];
        setReportingManager(uniqueManagers.map(m => ({ value: m, label: m })));

        // Filter base data
        let filteredData = rawDropdownData;

        if (newselectedManager && newselectedManager !== 'All') {
            filteredData = filteredData.filter(emp => emp.mashreqReportingManager === newselectedManager);
        }

        if (newselectedEmployee && newselectedEmployee !== 'All') {
            filteredData = filteredData.filter(emp => emp.employeeName === newselectedEmployee);
        }

        // Set Employees (based on selected manager)
        const employeeList = [
            ...new Set(
                (newselectedManager && newselectedManager !== 'All'
                    ? rawDropdownData.filter(emp => emp.mashreqReportingManager === newselectedManager)
                    : rawDropdownData
                ).map(emp => emp.employeeName).filter(Boolean))
        ];
        setEmployee(employeeList.map(e => ({ value: e, label: e })));

        // Set Projects (based on selected manager and employee)
        const projectList = [
            ...new Set(filteredData.map(emp => emp.projectName).filter(Boolean))
        ];
        setProject(projectList.map(p => ({ value: p, label: p })));

    }, [rawDropdownData, newselectedManager, newselectedEmployee]);


    const fetchSOWWorkStatus = async (status, setData) => {
        setChartLoading(true)
        try {
            const response = await axiosInstance.get(API_ENDPOINTS.getSOWStatus);
            setSowData([
                { id: 0, value: response.data.sowPendingCount, label: 'Pending' },
                { id: 1, value: response.data.sowSentCount, label: 'Sent' },
            ]);
        } catch (error) {
            console.error(`Error fetching SOW work status:`, error);
        }
        setChartLoading(false)
    };
    const fetchWO_POWorkStatus = async (status) => {
        setChartLoading(true)
        try {
            const response = await axiosInstance.get(API_ENDPOINTS.getPoStatus);
            setPoData([
                { id: 0, value: response.data.pending, label: 'Pending ' },
                { id: 1, value: response.data.recevied, label: 'Recevied ' },
            ]);
            // console.log("fetchEmployees", response.data);

        } catch (error) {
            console.error(`Error fetching SOW work status:`, error);
        }
        setChartLoading(false)
    };
    useEffect(() => {
        fetchEmployees();
    }, [selectedManager]);


    useEffect(() => {
        fetchEmployees();
        fetchSOWWorkStatus();
        fetchWO_POWorkStatus();
        setOpen(false);
        setUpdateOpen(false);
        setNewProjOpen(false);
        fetchEmployees();
        setHandleManagerOpen(false);

    }, [refresh]);

    const handleModelopen = (type) => {
        setType(type);
        setOpen(true);
    };
    const handleNewProjModel = (type) => {
        setNewProjOpen(true);
    };
    const handleMangerModel = () => {
        setHandleManagerOpen(true);
    }

    const handleEdit = (row) => {
        setSelectedEmployee(row);
        setUpdateOpen(true);
    };
    const handleDeleteConfirmed = async (employeeId, id) => {
        const confirmed = window.confirm("Are you sure you want to delete this record?");
        if (!confirmed) return;

        setLoading(true);
        try {

            await axiosInstance.delete(`${API_ENDPOINTS.deleteEmployee}/${id}`, {
                params: { employeeId }
            });
            setSnackbarOpen(true);
            fetchEmployees();
            fetchSOWWorkStatus();
            fetchWO_POWorkStatus();
        } catch (error) {
            console.error('Error deleting :', error);
        } finally {
            setDeleteConfirmOpen(false);
            setEmployeeToDelete(null);
        }
    };
    const statusDropDown = [
        { value: 'All', label: 'All' },
        { value: 'Sent', label: 'Sent' },
        { value: 'Pending', label: 'Pending' },
        { value: 'Received', label: 'Received' },
    ]
    const clearDropdowns1 = () => {
        setNewSelectedManager('');
        setNewSelectedEmployee('');
        setNewSelectedProject('');
        setSelectedStatus('');
        fetchEmployees('')
    };

    return (
        <div>
            <Grid2 container p={2} gap={2}>
                <Grid2 size={{ md: 0.2 }}></Grid2>

                {/* Chart 1 */}
                <Grid2 size={{ xs: 12, md: 4.4 }} mt={1} boxShadow={2} p={2} borderRadius={2}>
                    <Typography variant='h6' color='#207BBF'>SOW Status</Typography>
                    {chartLoading ? <CircularProgress size={40} /> : <BasicPie chartData={sowData} />}
                </Grid2>

                {/* Chart 2 */}
                <Grid2 size={{ xs: 12, md: 4.4 }} mt={1} boxShadow={2} p={2} borderRadius={2}>
                    <Typography variant='h6' color='#207BBF'>WO/PO Status</Typography>
                    {chartLoading ? <CircularProgress size={40} /> : <BasicPie chartData={poData} />}
                </Grid2>

                {/* Button Container */}
                <Grid2
                    size={{ xs: 12, md: 2.3 }}
                    mt={1}
                    boxShadow={2}
                    p={2}
                    borderRadius={2}
                    display="flex"
                    flexDirection="column"
                    justifyContent="center"
                    gap={2}
                >
                    <Button
                        variant='contained'
                        onClick={() => handleModelopen('contract')}
                        disabled={loading}
                        fullWidth
                         sx={{ textTransform: 'none' }}
                    >
                        {loading ? <CircularProgress size={24} /> : 'Add New Contract'}
                    </Button>

                    <Button
                        variant='contained'
                        onClick={() => handleNewProjModel()}
                        disabled={loading}
                        fullWidth
                         sx={{ textTransform: 'none' }}
                    >
                        {loading ? <CircularProgress size={24} /> : 'Add New Project'}
                    </Button>

                    <Button
                        variant='contained'
                        onClick={() => handleMangerModel()}
                        disabled={loading}
                        fullWidth
                         sx={{ textTransform: 'none' }}
                    >
                        {loading ? <CircularProgress size={24} /> : 'Add Manager Name'}
                    </Button>
                </Grid2>
            </Grid2>


            <Grid2 container p={1} textAlign={'end'} gap={2}>
                <Grid2 size={{ md: 1 }}></Grid2>
                <Grid2 size={{ xs: 12, md: 10 }} container gap={1} boxShadow={2} p={2}>
                    <Grid2 size={{ xs: 12, md: 2.4 }}>
                        <Dropdown
                            label="Reporting Manager"
                            fullWidth
                            options={[{ value: 'All', label: 'All' }, ...reportingManager]}
                            value={newselectedManager}
                            onChange={(e) => {
                                setNewSelectedManager(e.target.value);
                            }}
                        />
                    </Grid2>

                    <Grid2 size={{ xs: 12, md: 1.8 }}>
                        <Dropdown
                            label="Employee"
                            fullWidth
                            options={[{ value: 'All', label: 'All' }, ...employee]}
                            // options={employee}

                            value={newselectedEmployee}
                            onChange={(e) => {
                                setNewSelectedEmployee(e.target.value);
                                // setNewSelectedProject('All');
                            }}
                        />
                    </Grid2>

                    <Grid2 size={{ xs: 12, md: 1.7 }}>
                        <Dropdown
                            label="Project"
                            fullWidth
                            options={[{ value: 'All', label: 'All' }, ...project]}
                            // options={project}

                            value={newselectedProject}
                            onChange={(e) => setNewSelectedProject(e.target.value)}
                        />
                    </Grid2>
                    <Grid2 size={{ xs: 12, md: 1.6 }} >
                        <Dropdown
                            label="Status"
                            fullWidth
                            options={statusDropDown}
                            value={selectedStatus}
                            onChange={(e) => setSelectedStatus(e.target.value)}
                        />
                    </Grid2>
                    <Grid2 size={{ xs: 12, md: 1.1 }} sx={{ textTransform: 'none' }}>
                        <Button variant="contained"  sx={{ textTransform: 'none' }} onClick={filterData}>Search</Button>

                    </Grid2>
                    <Grid2 size={{ xs: 12, md: 1.3 }} sx={{ textTransform: 'none' }}>
                        <Button variant="contained"  sx={{ textTransform: 'none' }} onClick={sendReport}>Send Mail</Button>
                    </Grid2>
                    <Grid2 size={{ xs: 12, md: 1 }} sx={{ textTransform: 'none' }}>
                        <Button variant="contained"  sx={{ textTransform: 'none' }} onClick={clearDropdowns1} >clear</Button>
                    </Grid2>
                </Grid2>
            </Grid2>


            <Grid2 container mt={2}>
                <Grid2 size={{ md: 0.4 }}></Grid2>
                <Grid2 size={{ xs: 11.5 }} boxShadow={2} mb={3}>
                    {loading ? (
                        <Typography textAlign="center">Loading...</Typography>
                    ) : (
                        <CustomTable
                            backgroundColor={'#2E739F'}
                            columns={BaseColumns}
                            rows={employeeData?.map((row) => {
                                const isDeleteDisabled =
                                    row.sowStatus?.toLowerCase() === 'sent' &&
                                    (row.woStatus === 'Received' || row.poStatus === 'Received');

                                return {
                                    ...row,
                                    actions: (
                                        <IconButton onClick={() => handleEdit(row)}>
                                            <TbEyeEdit style={{ marginLeft: '10px', fontSize: '20px', color: 'black' }} />
                                        </IconButton>
                                    ),
                                    delete: (
                                        <IconButton
                                            onClick={() => handleDeleteConfirmed(row.employeeId, row.id)}
                                            disabled={isDeleteDisabled}
                                        >
                                            <MdDeleteOutline style={{ fontSize: '20px', color: isDeleteDisabled ? 'gray' : 'red' }} />
                                        </IconButton>
                                    )
                                };
                            })}
                        />

                    )}
                </Grid2>
            </Grid2>

            <CustomModal open={open} setOpen={setOpen} headder={`Add New Contract`}>
                <AddEmp type={type} />
            </CustomModal>

            <CustomModal open={newProjOpen} setOpen={setNewProjOpen} headder={`Add New Project`}>
                <AddNewProj />
            </CustomModal>


            <CustomModal open={updateOpen} setOpen={setUpdateOpen} headder={'Update Contract'}>
                <UpdateEmp employeeData={selectedEmployee} />
            </CustomModal>

            <CustomModal open={handleManagerOpen} setOpen={setHandleManagerOpen} headder={'Add Manager Name'}>
                <AddNewManger />
            </CustomModal>

            <Snackbar
                open={snackbarOpen}
                autoHideDuration={3000}
                onClose={() => setSnackbarOpen(false)}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}
            >
                <Alert onClose={() => setSnackbarOpen(false)} severity="success" sx={{ width: '100%' }}>
                    Deleted successfully!
                </Alert>
            </Snackbar>
        </div>
    );
}

export default Dashboard;
