import { Button, Card, FormControlLabel, Grid2, Snackbar, Switch, Typography, Alert } from '@mui/material';
import React, { useState } from 'react';
import SideImg from '../assets/eidiko-logo.svg';
import { useNavigate } from 'react-router-dom';
import CustomInputField from '../components/CustomInput';
import { API_ENDPOINTS } from '../api_properites/Api_URls';

function SignUp() {
    const navigate = useNavigate();
    const [userName, setUserName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setconfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [toggleValue, setToggleValue] = useState(false);

    const handlesignUp = async () => {
        if (!userName.trim() || !password.trim()) {
            setError('Please enter both username and password');
            return;
        }

        setError('');
        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        try {
            const response = await fetch(API_ENDPOINTS.signUp, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ userName, password }),
            });

            const data = await response.json();

            if (response.ok) {
                setSnackbarOpen(true); // ✅ Show snackbar
                setTimeout(() => {
                    navigate('/eidiko_workorder/'); // ✅ Navigate after 3 seconds
                }, 3000);
            } else {
                setError(data.message || 'Please try again');
            }
        } catch (error) {
            setError('Something went wrong. Please try again.');
        }
    };

    return (
        <div>
            <form onSubmit={(e) => { e.preventDefault(); handlesignUp(); }}>
                <Grid2 container mt={5}>
                    <Grid2 size={{ sm: 4 }}></Grid2>
                    <Grid2 container size={{ sm: 4 }}>
                        <Card sx={{ padding: 10, boxShadow: 3, borderRadius: 2 }} >
                            <Grid2 container gap={2}>
                                <Grid2 size={{ sm: 12 }} textAlign={'center'} mb={3}>
                                    <img src={SideImg} height={50} alt='img' />
                                </Grid2>

                                <Grid2 size={{ sm: 12 }}>
                                    <hr style={{ border: '0.5px solid #e0e0e0', marginBottom: '16px' }} />
                                </Grid2>

                                <Grid2 size={{ sm: 12 }}>
                                    <CustomInputField
                                        label={'UserName'}
                                        type={'text'}
                                        fullWidth
                                        value={userName}
                                        onChange={(e) => setUserName(e.target.value)}
                                    />
                                </Grid2>

                                <Grid2 size={{ sm: 12 }}>
                                    <CustomInputField
                                        label={'Password'}
                                        fullWidth
                                        type={'password'}
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                    />
                                </Grid2>
                                <Grid2 size={{ sm: 12 }}>
                                    <CustomInputField
                                        label={'Confirm Password'}
                                        fullWidth
                                        type={'password'}
                                        value={confirmPassword}
                                        onChange={(e) => setconfirmPassword(e.target.value)}
                                    />
                                </Grid2>

                                {error && (
                                    <Grid2 size={{ sm: 12 }}>
                                        <Typography color="error" textAlign="center">{error}</Typography>
                                    </Grid2>
                                )}

                                {/* ✅ Toggle shown after success */}
                                {/* {success && (
                                    // <Grid2 size={{ sm: 12 }} textAlign="center">
                                    //     <FormControlLabel
                                    //         control={
                                    //             <Switch
                                    //                 checked={toggleValue}
                                    //                 onChange={(e) => setToggleValue(e.target.checked)}
                                    //                 color="primary"
                                    //             />
                                    //         }
                                    //         // label={toggleValue ? "Enabled" : "Disabled"}
                                    //     />
                                    // </Grid2>
                                )} */}

                                <Grid2 size={{ sm: 12 }} textAlign={'center'}>
                                    <Button variant='contained' fullWidth type='submit' sx={{ textTransform: 'none' }}>
                                        Sign Up
                                    </Button>
                                </Grid2>
                                <Grid2 size={{ sm: 12 }} textAlign="center">
                                    <Typography variant="body2" color="textSecondary">
                                        — or —
                                    </Typography>
                                </Grid2>

                                <Grid2 size={{ sm: 12 }} textAlign="center">
                                    <Button
                                        variant="outlined"
                                        fullWidth
                                        onClick={() => navigate('/eidiko_workorder/')}
                                        sx={{ textTransform: 'none' }}
                                    >
                                        Login
                                    </Button>
                                </Grid2>
                            </Grid2>
                        </Card>
                    </Grid2>
                </Grid2>
            </form>

            {/* ✅ Snackbar after success */}
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={3000}
                onClose={() => setSnackbarOpen(false)}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
            >
                <Alert onClose={() => setSnackbarOpen(false)} severity="success" sx={{ width: '100%' }}>
                      Signup successful and mail sent successfully!
                </Alert>
            </Snackbar>

        </div>
    );
}

export default SignUp;
