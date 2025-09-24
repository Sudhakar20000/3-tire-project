import { Box, Button, Typography, Grid2, TextField } from '@mui/material';
import React, { useContext, useState } from 'react';
import CustomInputField from '../components/CustomInput';
import Dropdown from '../components/DropDown';
import axios from 'axios';
import { format } from 'date-fns';
import { EmployeeContext } from '../contexts/EmployeeContext';
import { API_ENDPOINTS } from '../api_properites/Api_URls';

function AddNewProj() {
  const { triggerRefresh } = useContext(EmployeeContext);
  const [formData, setFormData] = useState({
    projectName: '',
    startDate: '',
    projectType: 'Fixed',
    sowStatus: '',
    mashreqReportingManager: '',
    endDate: '',
    woStatus: '',
    poStatus: '',
    comments: '',
    poWoNumber: "",
    // woNumber: ""
  });


  const [errorMessage, setErrorMessage] = useState('');
  const [managerOptions, setManagerOptions] = useState([]);

  React.useEffect(() => {
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
          .map(m => ({ value: m.manager, label: m.manager }));

        setManagerOptions(formatted);
      } catch (error) {
        console.error('Failed to fetch managers', error);
      }
    };

    fetchManagers();
  }, []);




  const PO_Wo_Status = [
    { value: 'Pending', label: 'Pending' },
    { value: 'Received', label: 'Received' },
    { value: 'NA', label: 'NA' },
  ];

  const SOW_Status = [
    { value: 'Pending', label: 'Pending' },
    { value: 'Sent', label: 'Sent' },

  ];

  const handleSave = async () => {
    setErrorMessage('');
    const requiredFields = {
      projectName: 'Project Name',
      startDate: 'Start Date',
      projectType: 'Project Type',
      // sowStatus: 'SOW Status',
      mashreqReportingManager: 'Mashreq Reporting Manager Name',
      // endDate: 'End Date',
      // woStatus: 'WO Status',
      // poStatus: 'PO Status',
      // comments: 'Comments',
      poWoNumber: "PO Number",
    };


    const emptyFields = Object.keys(requiredFields).filter(
      (field) => !String(formData[field] || "").trim()
    );

    if (emptyFields.length === Object.keys(requiredFields).length) {
      setErrorMessage("Please fill all the required fields.");
      return;
    } else if (emptyFields.length > 0) {
      setErrorMessage(`Please fill the following required fields: ${emptyFields.map(field => requiredFields[field]).join(', ')}`);
      return;
    }
    const formattedData = {
      ...formData,
      startDate: formData.startDate ? format(new Date(formData.startDate), 'yyyy-MM-dd') : '',
      endDate: formData.endDate ? format(new Date(formData.endDate), 'yyyy-MM-dd') : '',
    };

    try {
      const token = localStorage.getItem('token');
      // console.log("Token:", token);
      // console.log("Form Data:", formData);

      const response = await axios.post(API_ENDPOINTS.addNewProject,
        formattedData,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );

      // console.log("Response:", response.data);
      alert('Project added successfully!');
      triggerRefresh();

      setFormData({
        projectName: '',
        startDate: '',
        projectType: 'Fixed',
        sowStatus: '',
        mashreqReportingManager: '',
        endDate: '',
        woStatus: '',
        poStatus: '',
        comments: '',
        poWoNumber: "",
        // woNumber: ""
      });

    } catch (error) {
      console.error('Error:', error);
      setErrorMessage(error.response?.data?.message || "Failed to add employee.");
    }
  };

  const handleChangeDate = (name, value) => {
    setFormData(prevState => {
      let updatedData = { ...prevState, [name]: value };

      if (name === "startDate") {
        const start = new Date(value);
        // if (!isNaN(start.getTime())) {
        //   const nextDay = new Date(start);
        //   nextDay.setDate(start.getDate() + 1);
        //   updatedData.endDate = nextDay.toISOString().split('T')[0];
        // }
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

  const woOptions = formData.woStatus === 'NA'
    ? [...PO_Wo_Status.filter(opt => opt.value !== 'NA'), { value: 'NA', label: 'NA' }]
    : PO_Wo_Status.filter(opt => opt.value !== 'NA');

  const poOptions = formData.poStatus === 'NA'
    ? [...PO_Wo_Status.filter(opt => opt.value !== 'NA'), { value: 'NA', label: 'NA' }]
    : PO_Wo_Status.filter(opt => opt.value !== 'NA');


  return (
    <Box>
      <Grid2 container gap={1} overflow={'auto'}>

        <Grid2 container size={{ sm: 12, md: 5.9 }} gap={1.5}>
          {/* {(type==='project')} */}
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
            />
          </Grid2>
          <Grid2 size={{ sm: 12 }}>
            <CustomInputField
              label="Start Date"
              type="date"
              fullWidth
              value={formData.startDate}
              onChange={(event) => handleChangeDate("startDate", event.target.value)}
            />

          </Grid2>
          <Grid2 size={{ sm: 12 }}>
            <CustomInputField
              label="Project Type"
              type="text"
              fullWidth
              value={formData.projectType}
            // onChange={(event) =>
            //     setFormData((prev) => ({
            //         ...prev,
            //         projectType: event.target.value,
            //     }))
            // }
            />
          </Grid2>
          <Grid2 size={{ sm: 12 }}>
            <Dropdown
              label="SOW Status"
              fullWidth
              options={SOW_Status}
              value={formData.sowStatus || ''}
              onChange={(event) =>
                setFormData((prev) => ({
                  ...prev,
                  sowStatus: event.target.value,
                }))
              } />

          </Grid2>
          <Grid2 size={{ sm: 12 }}>
            <CustomInputField
              // disabled
              // label="Po Number"
              fullWidth
            // type='number'
            // value={formData.poWoNumber || ""}
            // onChange={(event) =>
            //   setFormData((prev) => ({
            //     ...prev,
            //     poWoNumber: event.target.value,
            //   }))
            // }
            />
          </Grid2>

        </Grid2>


        <Grid2 container size={{ sm: 12, md: 5.9 }} gap={1.5}>
          <Grid2 size={{ sm: 12 }}>
            <Dropdown
              label="Mashreq Reporting Manager"
              fullWidth
              options={managerOptions}
              value={formData.mashreqReportingManager}
              onChange={(event) =>
                setFormData((prev) => ({
                  ...prev,
                  mashreqReportingManager: event.target.value,
                }))
              }
            />
          </Grid2>
          <Grid2 size={{ sm: 12 }}>
            <CustomInputField
              label="End Date"
              type="date"
              fullWidth
              value={formData.endDate}
              onChange={(event) => handleChangeDate("endDate", event.target.value)}
            // inputProps={{
            //   min: formData.startDate ? formData.startDate : "",
            // }}
            />


          </Grid2>
          <Grid2 size={{ sm: 12 }}>
            <Dropdown
              label="WO Status"
              fullWidth
              options={woOptions}
              value={formData.woStatus || ''}
              disabled={formData.poStatus && formData.poStatus !== '' && formData.poStatus !== 'NA'}
              onChange={(event) => {
                const selectedWO = event.target.value;
                setFormData((prev) => ({
                  ...prev,
                  woStatus: selectedWO,
                  poStatus: selectedWO !== '' ? 'NA' : '',
                }));
              }}
            />
          </Grid2>

          <Grid2 size={{ sm: 12 }} md={10}>
            <Dropdown
              label="PO Status"
              fullWidth
              options={poOptions}
              value={formData.poStatus || ''}
              disabled={formData.woStatus && formData.woStatus !== '' && formData.woStatus !== 'NA'}
              onChange={(event) => {
                const selectedPO = event.target.value;
                setFormData((prev) => ({
                  ...prev,
                  poStatus: selectedPO,
                  woStatus: selectedPO !== '' ? 'NA' : '',
                }));
              }}
            />
          </Grid2>

          <Grid2 size={{ sm: 12 }}>
            <CustomInputField
              label="PO Number"
              fullWidth
              type='number'
              value={formData.poWoNumber || ""}
              onChange={(event) =>
                setFormData((prev) => ({
                  ...prev,
                  poWoNumber: event.target.value,
                }))
              }
            />
          </Grid2>
        </Grid2>
        <Grid2 size={{ sm: 12 }} mt={1}>
          <CustomInputField
            height='90px'
            label="Comments"
            type="text"
            fullWidth
            value={formData.comments}
            onChange={(event) =>
              setFormData((prev) => ({
                ...prev,
                comments: event.target.value,
              }))
            } />
        </Grid2>
      </Grid2>


      {errorMessage && (
        <Typography color="error" mt={2} textAlign="center">{errorMessage}</Typography>
      )}


      <Grid2 container m={1} mt={2} justifyContent="flex-end" gap={1}>
        <Grid2>
          <Button variant="contained" color="primary" type='submit' onClick={handleSave} sx={{ textTransform: 'none' }}>
            Save
          </Button>
        </Grid2>
        <Grid2>
          <Button
           sx={{ textTransform: 'none' }}
            variant="contained"
            color="secondary"
            onClick={() =>
              setFormData({
                projectName: '',
                startDate: '',
                projectType: 'Fixed',
                sowStatus: '',
                mashreqReportingManager: '',
                endDate: '',
                woStatus: '',
                poStatus: '',
                comments: '',
                poWoNumber: '',
              })
            }
          >
            Clear
          </Button>
        </Grid2>
      </Grid2>
    </Box >
  );
}

export default AddNewProj;
