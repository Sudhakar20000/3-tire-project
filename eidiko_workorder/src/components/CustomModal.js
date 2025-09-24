import React, { useContext, useState } from "react";
import { Modal, Box, Button, Typography } from "@mui/material";
import { FaTimes } from "react-icons/fa";
import { EmployeeContext } from "../contexts/EmployeeContext";

const CustomModal = ({ open, setOpen, children,headder }) => {
 const { triggerRefresh } = useContext(EmployeeContext);

    const handleClose = () => {
        setOpen(false);
    }

    // if(setOpen(false)){
    //     triggerRefresh();
    // }

    return (
        <Modal open={open} 
        BackdropProps={{
            sx: {
              backgroundColor: "rgba(0, 0, 0, 0.4)",
              backdropFilter: "blur(5px)",
              WebkitBackdropFilter: "blur(5px)",
            },
          }}>
            <Box 
                sx={{
                    position: "absolute",
                    top: "50%",
                    left: "50%",
                    transform: "translate(-50%, -50%)",
                    bgcolor: "background.paper",                              
                    boxShadow: 24,
                    p: 3,
                    borderRadius: 2,
                    textAlign: "center",   
                 
                }}
            >
                <Box sx={{display:'flex',flexDirection:'row'}}>
                    <Box mb={3}>
                     <Typography>{headder}</Typography>
                    </Box>
                    <button
                        onClick={handleClose}
                        style={{
                            position: "absolute",
                            top: 14,
                            right: 10,
                            background: "none",
                            border: "none",
                            cursor: "pointer",
                        }}
                    >
                        <FaTimes color="red" size={24} /> 
                    </button>
                </Box>
                {children}
            </Box>
        </Modal>
    );
};

export default CustomModal;
