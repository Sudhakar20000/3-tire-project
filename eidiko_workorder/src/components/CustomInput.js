import React, { useState } from 'react';
import TextField from '@mui/material/TextField';

function CustomInputField({ 
    label, 
    placeholder, 
    value, 
    onChange, 
    required = false, 
    fullWidth = false, 
    type, 
    disabled = false,
    height = "43px"  || height
}) {
    const [isFocused, setIsFocused] = useState(false);
  
    return (
        <TextField
            required={required}
            label={label}
            type={type}
            placeholder={placeholder}
            value={value}
            onFocus={() => setIsFocused(true)}
            onBlur={() => setIsFocused(false)}
            onChange={onChange}
            fullWidth={fullWidth}
            disabled={disabled}
            InputLabelProps={{
                shrink: type === 'date' ? true : undefined,
                style: {
                    color: isFocused ? '#055B50' : '#055B50',
                    fontSize: '18px',
                },
            }}
            sx={{
                '& .MuiOutlinedInput-root': {
                    '& fieldset': {
                        borderColor: 'black',
                    },
                    '&:hover fieldset': {
                        borderColor: 'black',
                    },
                    '&.Mui-focused fieldset': {
                        borderColor: 'black',
                    },
                    "& input": {
                        padding: "10px 14px",
                        height: height, 
                        display: "flex",
                        alignItems: "center",
                        boxSizing: "border-box",
                        fontSize: "18px",
                        width: "100%",
                    },
                },
                '& .MuiInputLabel-root': {
                    top: '-6px',
                },
                '& .MuiInputLabel-shrink': {
                    top: '0px',
                }
            }}
        />
    );
}

export default CustomInputField;
