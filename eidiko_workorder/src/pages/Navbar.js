import React, { useState } from 'react';
import { AppBar, Toolbar, Typography, Avatar, Box, Card, IconButton, Tooltip, Alert } from '@mui/material';
import logo from '../assets/eidiko-logo.svg';
import { HiLogout } from "react-icons/hi";
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { API_ENDPOINTS } from '../api_properites/Api_URls';

function Navbar({ username }) {
  const navigate = useNavigate();
  const [hovered, setHovered] = useState(false);

  const formattedUsername = username ? username.charAt(0).toUpperCase() + username.slice(1) : "User";

  const handleLogOut = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.clear();
    navigate('/eidiko_workorder/')
  };

  return (
    <Card sx={{ backgroundColor: 'white', boxShadow: 3, borderRadius: 2, p: 0.2 }}>
      <AppBar position="static" sx={{ backgroundColor: 'transparent', boxShadow: 'none', minHeight: 40 }}>
        <Toolbar sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', minHeight: 40 }}>
          <Box sx={{ width: 40 }} />
          <img src={logo} alt='img' style={{ height: 30, objectFit: 'contain' }} />
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            {/* Logout Icon with hover */}
            <Tooltip title="Logout" arrow>
              <IconButton
                onClick={handleLogOut}
                onMouseEnter={() => setHovered(true)}
                onMouseLeave={() => setHovered(false)}
                sx={{ mr: 1 }}
              >
                <HiLogout size={32} color={hovered ? 'red' : 'orange'} />
              </IconButton>
            </Tooltip>

            {/* Avatar with username */}
            <Avatar sx={{ width: 30, height: 30, bgcolor: 'gray', padding: '2px', mr: 1 }}>
              {formattedUsername.charAt(0)}
            </Avatar>

            <Typography variant="h6" color="#207BBF" width="auto">
              {formattedUsername}
            </Typography>
          </Box>

        </Toolbar>
      </AppBar>
    </Card>
  );
}

export default Navbar;
