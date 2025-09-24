import { Box, Button, Grid2, Typography } from '@mui/material';
import React, { useState, useEffect, useContext } from 'react';
import Dropdown from '../components/DropDown';
import CustomInputField from '../components/CustomInput';
import axios from 'axios';
import { EmployeeContext } from '../contexts/EmployeeContext';
import { format } from 'date-fns';
import { API_ENDPOINTS } from '../api_properites/Api_URls';
import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';
import CustomTable from '../components/CustomTable';
import { updateed_audittrail_columns } from '../Constants/lable';

function UpdateEmp({ employeeData }) {
    const { triggerRefresh } = useContext(EmployeeContext);
    console.log("emploupdate", employeeData);
    const [formData, setFormData] = useState({
        empId: '',
        employeeName: '',
        startDate: '',
        endDate: '',
        projectType: '',
        sowStatus: '',
        woStatus: '',
        poStatus: '',
        comments: '',
        projectName: '',
        mashreqReportingManager: '',
        poWoNumber: "",

    });
    const [isUpdateCompleted, setIsUpdateCompleted] = useState(true);
    const [managerOptions, setManagerOptions] = useState([]);


    const updatedBy = localStorage.getItem("username");

    const auditRows = [
        {
            auditAction: employeeData?.auditAction || '',
            createdUpdatedBy: employeeData?.createdUpdatedBy || '',
            timestamp: employeeData?.timestamp
                ? (() => {
                    const dateObj = new Date(employeeData.timestamp);

                    const day = String(dateObj.getDate()).padStart(2, '0');
                    const month = String(dateObj.getMonth() + 1).padStart(2, '0');
                    const year = dateObj.getFullYear();

                    let hours = dateObj.getHours();
                    const minutes = String(dateObj.getMinutes()).padStart(2, '0');
                    const ampm = hours >= 12 ? 'pm' : 'am';
                    hours = hours % 12 || 12;

                    return `${day}-${month}-${year} ${String(hours).padStart(2, '0')}:${minutes} ${ampm}`;
                })()
                : '',
            auditUnitTest: (() => {
                const test = employeeData?.auditUnitTest || '';

                // Split by comma and filter out null entries
                const parts = test.split(',').map(part => part.trim());

                const filtered = parts.filter(part =>
                    !part.toLowerCase().includes('null -')
                );

                return filtered.join(', ');
            })(),
        },
    ];


    // console.log(formData.empId, "empId");

    const [errorMessage, setErrorMessage] = useState('');
    dayjs.extend(customParseFormat);
    useEffect(() => {
        if (employeeData) {
            setFormData({
                empId: employeeData.employeeId || '',
                employeeName: employeeData.employeeName || '',
                startDate: employeeData.startDate
                    ? dayjs(employeeData.startDate).format('YYYY-MM-DD')
                    : '',
                endDate: employeeData.endDate
                    ? dayjs(employeeData.endDate).format('YYYY-MM-DD')
                    : '',
                projectType: employeeData.projectType || '',
                sowStatus: employeeData.sowStatus || '',
                woStatus: employeeData.woStatus || '',
                poStatus: employeeData.poStatus || '',
                comments: employeeData.comments || '',
                projectName: employeeData.projectName || '',
                mashreqReportingManager: employeeData.mashreqReportingManager || '',
                poWoNumber: employeeData.poWoNumber || '',
                // woNumber: employeeData.woNumber || ''

            });
            fetchManagers();
        }
    }, [employeeData]);


    const fetchManagers = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.get(API_ENDPOINTS.getMashreqReportingManagers, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            const managers = response.data.result.managers;

            const formatted = managers
                .filter((m) => m.manager !== null)
                .map((m) => ({ value: m.manager, label: m.manager }));

            setManagerOptions(formatted); // ✅ Save in state
        } catch (error) {
            console.error('Failed to fetch managers', error);
        }
    };


    const Wo_Po_Status = [
        { value: "Pending", label: "Pending" },
        { value: 'Received', label: 'Received' },
    ];
    const sowStatus = [
        { value: "Pending", label: "Pending" },
        { value: "Sent", label: "Sent" },
    ];


    const woOptions = formData.woStatus === 'NA'
        ? [...Wo_Po_Status.filter(opt => opt.value !== 'NA'), { value: 'NA', label: 'NA' }]
        : Wo_Po_Status.filter(opt => opt.value !== 'NA');

    const poOptions = formData.poStatus === 'NA'
        ? [...Wo_Po_Status.filter(opt => opt.value !== 'NA'), { value: 'NA', label: 'NA' }]
        : Wo_Po_Status.filter(opt => opt.value !== 'NA');

    const handleSave = async () => {
        const token = localStorage.getItem('token');
        setErrorMessage('');

        const requiredFields = {
            projectName: 'Project Name',
            projectType: 'Project Type',
            // sowStatus: 'SOW Status',
            // woStatus: 'WO Status',
            // poStatus: 'PO Status',
            // endDate: 'End Date',
            // comments: 'Comments',
            mashreqReportingManager: 'Reporting Manager',

        };

        const emptyFields = Object.keys(requiredFields).filter(
            field => !formData[field]?.toString().trim()
        );

        if (emptyFields.length > 0) {
            setErrorMessage(
                `Please fill the following required fields: ${emptyFields.map(field => requiredFields[field]).join(', ')}`
            );
            return;
        }

        // ✅ Validate endDate before using format()
        // const parsedEndDate = new Date(formData.endDate);
        // if (isNaN(parsedEndDate)) {
        //     setErrorMessage("Please provide a valid End Date.");
        //     return;
        // }

        const putData = {
            endDate: formData.endDate ? format(new Date(formData.endDate), 'yyyy-MM-dd') : '',
            projectName: formData.projectName.trim(),
            projectType: formData.projectType,
            sowStatus: formData.sowStatus,
            woStatus: formData.woStatus,
            poStatus: formData.poStatus,
            comments: formData.comments.trim(),
            mashreqReportingManager: formData.mashreqReportingManager,
            poWoNumber: formData.poWoNumber,
            // woNumber: formData.woNumber,
            createdUpdatedBy: updatedBy
        };

        const endpoint = `${API_ENDPOINTS.updateEmployeeOrProject}/${employeeData.id}`;

        try {
            const response = await axios.put(endpoint, putData, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                params: {
                    employeeId: employeeData.employeeId
                }
            });

            // console.log("Response:", response.data);
            alert('Update successful!');
            setIsUpdateCompleted(true);
            triggerRefresh();
        } catch (error) {
            console.error('Error:', error);
            setErrorMessage(error.response?.data?.message || "Failed to update.");
        }
    };

    const handleChangeDate = (name, value) => {
        setFormData(prevState => {
            let updatedData = { ...prevState, [name]: value };

            if (name === "startDate") {
                const start = new Date(value);
                if (!isNaN(start.getTime())) {
                    const nextDay = new Date(start);
                    nextDay.setDate(start.getDate() + 1);
                    updatedData.endDate = nextDay.toISOString().split('T')[0];
                }
            } else if (name === "endDate") {
                const start = new Date(prevState.startDate);
                const end = new Date(value);

                if (end < start) {
                    alert("End date cannot be before the start date!");
                    return prevState;
                }
            }

            return updatedData;
        });
    };


    // const isFormDisabled = isUpdateCompleted && (
    //     (formData.woStatus === "Received" || formData.poStatus === "Received") &&
    //     formData.sowStatus?.toLowerCase() === "sent"
    // );

    const isFormDisabled =
        employeeData.sowStatus === "Sent" &&
        (employeeData.woStatus === "Received" || employeeData.poStatus === "Received");

    return (
        <Box>
            <Grid2 container gap={1}>
                <Grid2 container size={{ sm: 6, md: 5.9 }} gap={1}>
                    {formData.empId && String(formData.empId || "").trim() !== '' && (
                        <Grid2 size={{ sm: 12 }} >
                            <CustomInputField
                                label={'EmpId'}
                                type={'text'}
                                fullWidth
                                name="empId"
                                value={formData.empId}
                                disabled
                            />
                        </Grid2>
                    )}
                    <Grid2 size={{ sm: 12 }}>
                        <CustomInputField
                            label={'Start Date'}
                            type={'date'}
                            fullWidth
                            name="startDate"
                            value={formData.startDate}
                            disabled
                        // onChange={(event) => handleChangeDate("startDate", event.target.value)}
                        />

                    </Grid2>
                    <Grid2 size={{ sm: 12 }}>
                        <CustomInputField
                            label={'Project Type'}
                            type={'text'}
                            fullWidth
                            name="projectType"
                            value={formData.projectType}
                            onChange={(event) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    projectType: event.target.value,
                                }))
                            }
                            disabled
                        />
                    </Grid2>
                    <Grid2 size={{ sm: 12 }}>
                        <Dropdown
                            label={'SOW Status'}
                            fullWidth
                            name="sowStatus"
                            value={formData.sowStatus}
                            onChange={(event) => {
                                // console.log("Dropdown changed:", event.target.value);
                                setFormData((prev) => ({
                                    ...prev,
                                    sowStatus: event.target.value,
                                }));
                            }}

                            options={sowStatus}
                            disabled={isFormDisabled}
                        />
                    </Grid2>
                    <Grid2 size={{ sm: 12 }}>
                        <Dropdown
                            label="Mashreq Reporting Manager"
                            name="mashreqReportingManager"
                            fullWidth
                            value={formData.mashreqReportingManager}
                            onChange={(event) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    mashreqReportingManager: event.target.value,
                                }))
                            }
                            options={managerOptions}
                            disabled={isFormDisabled}
                        />
                    </Grid2>

                    <Grid2 size={{ sm: 12 }} >
                        <CustomInputField
                            height='40px'
                            label="Comments"
                            type="text"
                            fullWidth
                            value={formData.comments}
                            onChange={(event) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    comments: event.target.value,
                                }))
                            }
                            disabled={isFormDisabled}
                        />
                    </Grid2>
                    <Grid2 size={{ sm: 12 }}>
                        <CustomInputField
                            label="WO/PO Number"
                            fullWidth
                            type='number'
                            value={formData.poWoNumber || ""}
                            onChange={(event) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    poWoNumber: event.target.value,
                                }))
                            }
                            disabled={isFormDisabled}
                        />
                    </Grid2>
                </Grid2>
                <Grid2 container size={{ sm: 6, md: 5.9 }} gap={1}>
                    {formData.employeeName && formData.employeeName.trim() !== '' && (
                        <Grid2 size={{ sm: 12 }} >
                            <CustomInputField
                                label={'Employee Name'}
                                type={'text'}
                                fullWidth
                                name="employeeName"
                                value={formData.employeeName}
                                disabled
                            />
                        </Grid2>
                    )}

                    <Grid2 size={{ sm: 12 }}>
                        <CustomInputField
                            label="End Date"
                            type="date"
                            fullWidth
                            value={formData.endDate}
                            onChange={(event) => handleChangeDate("endDate", event.target.value)}
                            // inputProps={{
                            //     min: formData.startDate ? formData.startDate : "",
                            // }}
                            disabled={isFormDisabled}
                        />

                    </Grid2>
                    <Grid2 size={{ sm: 12 }}>
                        <Dropdown
                            label="WO Status"
                            fullWidth
                            options={woOptions}
                            value={formData.woStatus || ''}
                            // disabled={formData.poStatus && formData.poStatus !== '' && formData.poStatus !== 'NA'}
                            onChange={(event) => {
                                const selectedWO = event.target.value;
                                setFormData((prev) => ({
                                    ...prev,
                                    woStatus: selectedWO,
                                    poStatus: selectedWO !== '' ? 'NA' : '',
                                }));
                            }}
                            disabled={isFormDisabled}
                        />
                    </Grid2>

                    <Grid2 size={{ sm: 12 }} md={10}>
                        <Dropdown
                            label="PO Status"
                            fullWidth
                            options={poOptions}
                            value={formData.poStatus || ''}
                            // disabled={formData.woStatus && formData.woStatus !== '' && formData.woStatus !== 'NA'}
                            onChange={(event) => {
                                const selectedPO = event.target.value;
                                setFormData((prev) => ({
                                    ...prev,
                                    poStatus: selectedPO,
                                    woStatus: selectedPO !== '' ? 'NA' : '',
                                }));
                            }}
                            disabled={isFormDisabled}
                        />
                    </Grid2>
                    <Grid2 size={{ sm: 12 }}>
                        <CustomInputField
                            label="Project Name"
                            type="text"
                            fullWidth
                            value={formData.projectName}
                            onChange={(event) =>
                                setFormData((prev) => ({
                                    ...prev,
                                    projectName: event.target.value,
                                }))
                            }
                            disabled={isFormDisabled}
                        />
                    </Grid2>
                    <Grid2 size={{ sm: 12 }}>
                        <CustomInputField
                            fullWidth
                        // label="Wo Number"
                        // fullWidth
                        // type='number'
                        // value={formData.woNumber || ""}
                        // onChange={(event) =>
                        //     setFormData((prev) => ({
                        //         ...prev,
                        //         woNumber: event.target.value,
                        //     }))
                        // }
                        disabled
                        />
                    </Grid2>
                    <Grid2 size={{ sm: 12 }}>
                        <CustomInputField
                            fullWidth
                            disabled
                        // label="Wo Number"
                        // fullWidth
                        // type='number'
                        // value={formData.woNumber || ""}
                        // onChange={(event) =>
                        //     setFormData((prev) => ({
                        //         ...prev,
                        //         woNumber: event.target.value,
                        //     }))
                        // }
                        />
                    </Grid2>
                </Grid2>

            </Grid2>

            {
                errorMessage && (
                    <Typography color="error" mt={2} textAlign="center">{errorMessage}</Typography>
                )
            }

            <Grid2 container m={1} mt={2} gap={4}>
                <Grid2 size={{ sm: 9 }}></Grid2>
                <Grid2 size={{ sm: 1.2 }}>
                    <Button
                        variant="contained"
                        color="primary"
                        disabled={isFormDisabled}
                        onClick={handleSave}
                        sx={{ textTransform: 'none' }}
                    >
                        Update
                    </Button>

                </Grid2>
                {/* <Grid2 size={{ sm: 1 }} >
                    <Button variant='contained'
                        onClick={() =>
                            setFormData({
                                projectType: '',
                                sowStatus: '',
                                endDate: '',
                                woStatus: '',
                                poStatus: '',
                                comments: ''
                            })
                        }
                    > Clear</Button>
                </Grid2> */}
            </Grid2>
            <Grid2>
                <CustomTable
                    showPagination={false}
                    backgroundColor="#1F89CD"
                    columns={updateed_audittrail_columns}
                    rows={auditRows}
                />

            </Grid2>
        </Box >
    );
}

export default UpdateEmp;
