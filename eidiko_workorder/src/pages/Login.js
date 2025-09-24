import { Button, Card, FormControl, Grid2, Typography,CircularProgress  } from '@mui/material'
import React, { useState } from 'react'
import SideImg from '../assets/eidiko-logo.svg'
import { useNavigate } from 'react-router-dom'
import CustomInputField from '../components/CustomInput';
import { API_ENDPOINTS } from '../api_properites/Api_URls'

function Login() {
    const navigate = useNavigate();
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);


    const handleLogin = async () => {
        setError('');
        setLoading(true); 

        if (!userName.trim() || !password.trim()) {
            setError('Please enter both username and password');
            setLoading(false); 
            return;
        }

        try {
            const response = await fetch(API_ENDPOINTS.login, {
                
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ userName, password }),
            });

            const data = await response.json();

            if (response.ok && data.token) {
                localStorage.setItem('token', data.token);
                localStorage.setItem('username', userName);
                navigate('/eidiko_workorder/dashboard');
            } else {
                setError(data.message || 'Invalid credentials');
            }
        } catch (error) {
            setError('Something went wrong. Please try again.');
        } finally {
            setLoading(false); // Stop loading after all
        }
    };



    return (
        <div>
            <form onSubmit={(e) => { e.preventDefault(); handleLogin(); }}>
                <Grid2 container mt={6}>
                    <Grid2 size={{ sm: 4 }}></Grid2>
                    <Grid2 container size={{ sm: 4 }}>
                        <Card sx={{ padding: 8, boxShadow: 3, borderRadius: 2 }} >
                            <Grid2 container gap={2}  >
                                <Grid2 size={{ sm: 12 }} textAlign={'center'} mb={3}>
                                    <img src={SideImg} height={50} alt='img' />
                                </Grid2>

                                <Grid2 size={{ sm: 12 }}>
                                    <hr style={{ border: '0.5px solid #e0e0e0', marginBottom: '16px' }} />
                                </Grid2>

                                <Grid2 size={{ sm: 12 }}>
                                    <CustomInputField
                                        label={'UserName'}
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

                                {error && (
                                    <Grid2 size={{ sm: 12 }}>
                                        <Typography color="error" textAlign="center">{error}</Typography>
                                    </Grid2>
                                )}

                                <Grid2 size={{ sm: 12 }} textAlign="center">
                                    <Button
                                        variant="contained"
                                        fullWidth
                                        type="submit"
                                        disabled={loading}
                                        sx={{ textTransform: 'none' }}
                                    >
                                        {loading ? <CircularProgress size={24} color="inherit" /> : 'Login'}
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
                                        onClick={() => navigate('/eidiko_workorder/signUp')}
                                        sx={{ textTransform: 'none' }}
                                    >
                                        Sign up
                                    </Button>
                                </Grid2>
                            </Grid2>
                        </Card>
                    </Grid2>
                </Grid2>
            </form>
        </div>
    );
}

export default Login;
