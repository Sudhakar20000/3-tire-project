import React, { useContext, useState, useEffect } from 'react';
import axios from 'axios';
import CustomInputField from '../components/CustomInput';
import { Grid2, Button } from '@mui/material';
import { API_ENDPOINTS } from '../api_properites/Api_URls';
import { EmployeeContext } from '../contexts/EmployeeContext';
import Dropdown from '../components/DropDown';

function AddNewManager() {
    const { triggerRefresh } = useContext(EmployeeContext);

    const [managerName, setManagerName] = useState('');
    const [managerOptions, setManagerOptions] = useState([]);
    const [selectedManager, setSelectedManager] = useState(''); 
    console.log(selectedManager,"selected");
    

    // Add new manager
    const handleSubmit = async () => {
        if (!managerName.trim()) {
            alert('Manager field cannot be empty!');
            return;
        }

        const token = localStorage.getItem('token');

        try {
            const response = await axios.post(
                API_ENDPOINTS.addManager,
                { manager: managerName },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                }
            );
            console.log('Manager Added:', response.data);
            alert('Manager added successfully!');
            triggerRefresh();
            setManagerName('');
            fetchManagers(); // Refresh manager list
        } catch (error) {
            console.error('Error adding manager:', error);
            alert('Failed to add manager.');
        }
    };

    // Fetch all managers
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
                .map(m => ({ value: m.id, label: m.manager })); // Use id as value

            setManagerOptions(formatted);
        } catch (error) {
            console.error('Failed to fetch managers', error);
        }
    };

    useEffect(() => {
        fetchManagers();
    }, []);

    // Delete manager by ID
    const deleteManager = async () => {
        if (!selectedManager) {
            alert('Please select a manager to delete.');
            return;
        }

        const confirmDelete = window.confirm('Are you sure you want to delete this manager?');
        if (!confirmDelete) return;

        try {
            const token = localStorage.getItem('token');

            const response = await axios.delete(
                `${API_ENDPOINTS.deleteManager}/${selectedManager}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            alert('Manager deleted successfully!');
            console.log('Deleted:', response.data);
            fetchManagers(); // Refresh list
            setSelectedManager('');
        } catch (error) {
            console.error('Failed to delete manager:', error);
            alert('Deletion failed.');
        }
    };

    return (
        <div>
            <Grid2 container spacing={2} direction="column" p={1}>
                <Grid2 sm={12}>
                    <CustomInputField
                        height="50px"
                        label="Add Manager Name"
                        value={managerName}
                        fullWidth
                        onChange={(e) => setManagerName(e.target.value)}
                    />
                </Grid2>

                <Grid2 sm={12}>
                    <Button variant="contained" color="primary" onClick={handleSubmit} sx={{ textTransform: 'none' }}>
                        Submit
                    </Button>
                </Grid2>

                <Grid2 sm={12} p={1}>
                    <Dropdown
                        label="Reporting Manager Name"
                        fullWidth
                        options={managerOptions}
                        value={selectedManager}
                        onChange={(event) => setSelectedManager(event.target.value)}
                    />
                </Grid2>

                <Grid2 sm={12}>
                    <Button variant="contained" color="error" onClick={deleteManager} sx={{ textTransform: 'none' }}>
                        Delete Manager
                    </Button>
                </Grid2>
            </Grid2>
        </div>
    );
}

export default AddNewManager;
