import React, { useState } from "react";
import { Modal, Box, Button, Typography } from "@mui/material";
import { FaTimes } from "react-icons/fa";

const CustomModal = ({ open, setOpen, children }) => {


    const handleClose = () => {
        setOpen(false);
    }

    return (
        <Modal open={open} onClose={handleClose}>
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
                     <Typography>Header</Typography>
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
