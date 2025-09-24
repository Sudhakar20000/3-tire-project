import React, { useState } from 'react';
import {
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    FormHelperText,
} from '@mui/material';

const Dropdown = ({
    label,
    value,
    onChange,
    options = [],
    required = false,
    error = false,
    helperText = '',
    fullWidth = false,
    sx = {},
    disabled= false,
}) => {
    const [isFocused, setIsFocused] = useState(false);

    return (
        <FormControl
            required={required}
            error={error}
            fullWidth={fullWidth}
            disabled={disabled}
            sx={{
                backgroundColor: 'white',
                ...sx,
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
                },
            }}
        >
            <InputLabel
                shrink={!!value || isFocused}         
                sx={{
                    color: isFocused ? '#055B50' : '#055B50',
                    transform: (!!value || isFocused) 
                        ? "translate(14px, -10px) scale(0.85)" 
                        : "translate(14px, 12px) scale(1)",
                    transition: "all 0.2s ease-in-out",
                    backgroundColor: 'white', 
                    padding: '0 4px', 
                    '&.Mui-focused': {
                        color: '#055B50',
                    },
                }}
            >
                {label}
            </InputLabel>
            <Select
                value={value}
                onChange={onChange}
                onFocus={() => setIsFocused(true)}
                onBlur={() => setIsFocused(false)}
                displayEmpty
                MenuProps={{
                    MenuListProps: {
                        onMouseLeave: () => document.activeElement.blur(),
                    },
                }}
                sx={{
                    height: '43px',
                    '& .MuiSelect-select': {
                        padding: '10px 14px',
                        display: 'flex',
                        alignItems: 'center',
                        height: '45px', 
                         width: "100%",
                    },
                }}
            >
                {options.map((option, index) => (
                    <MenuItem key={index} value={option.value}>
                        {option.label}
                    </MenuItem>
                ))}
            </Select>
            {helperText && <FormHelperText>{helperText}</FormHelperText>}
        </FormControl>
    );
};

export default Dropdown;
