import {
    Box, Button, Paper, Table, TableBody, TableCell, Checkbox,
    TableContainer, TableHead, TableRow, TextField, Typography
} from '@mui/material';
import { useState, useEffect } from 'react';

const CustomTable = ({ columns = [], rows = [], rowsPerPageOptions = [5, 10, 20], primaryColor = '#055B50', showCheckbox = false, selectedRows, setSelectedRows, showPagination = true, backgroundColor, width }) => {
    const [selectAll, setSelectAll] = useState(false);
    const [isValid, setIsValid] = useState(false);
    const [rowsPerPage, setRowsPerPage] = useState(rowsPerPageOptions[0]);
    const [currentPage, setCurrentPage] = useState(1);
    console.log(showCheckbox, "showcheckbox");


    useEffect(() => {
        if (Array.isArray(columns) && Array.isArray(rows)) {
            setIsValid(true);
        } else {
            console.error("Invalid data: columns or rows are not arrays.");
            setIsValid(false);
        }
    }, [columns, rows]);

    if (!isValid) {
        return <Typography color="error">Invalid data passed to the table. Please check your columns and rows.</Typography>;
    }

    const totalPages = Math.ceil(rows.length / rowsPerPage);

    const handleRowsPerPageChange = (event) => {
        const value = Math.max(1, parseInt(event.target.value, 10));
        setRowsPerPage(value);
        setCurrentPage(1);
    };

    const handleNextPage = () => {
        if (currentPage < totalPages) setCurrentPage((prev) => prev + 1);
    };

    const handlePreviousPage = () => {
        if (currentPage > 1) setCurrentPage((prev) => prev - 1);
    };

    const startIndex = (currentPage - 1) * rowsPerPage;
    const displayedRows = rows.slice(startIndex, startIndex + rowsPerPage);


    const handleCheckboxChange = (applicationNumber) => {
        setSelectedRows((prevSelected) =>
            prevSelected.includes(applicationNumber)
                ? prevSelected.filter((rowId) => rowId !== applicationNumber)
                : [...prevSelected, applicationNumber]
        );
    };
    return (
        <Box sx={{ padding: 2 }}>

            {/* {showPagination && (
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', marginBottom: 1 }}>
                    <Typography variant="body1" sx={{ color: 'black', marginRight: 1 }}>
                        Per page:
                    </Typography>
                    <TextField
                        type="number"
                        value={rowsPerPage}
                        onChange={handleRowsPerPageChange}
                        variant="outlined"
                        size="small"
                        sx={{
                            width: '100px', marginRight: 2, backgroundColor: 'white',
                            '& .MuiOutlinedInput-root': {
                                '& fieldset': { borderColor: primaryColor },
                                '&:hover fieldset': { borderColor: primaryColor },
                                '&.Mui-focused fieldset': { borderColor: primaryColor },
                            },
                        }}
                    />
                </Box> */}
            {/* )} */}



            <TableContainer component={Paper} sx={{ backgroundColor: 'white', overflowX: 'auto' }}  >
                <Table sx={{ minWidth: 650, borderCollapse: 'collapse' }} aria-label="custom table" >
                    <TableHead sx={{ backgroundColor: backgroundColor, }}>
                        <TableRow>
                            {showCheckbox && (
                                <TableCell align="center" sx={{ color: 'black', border: '1px solid #D3D3D5', fontWeight: 'bold' }}>
                                    Select
                                </TableCell>
                            )}
                            {columns.map((column) => (
                                <TableCell
                                    key={column.id}
                                    align="center"
                                    style={{ width: column.width || width }}
                                    sx={{
                                        color: 'white',
                                        border: '1px solid #D3D3D5',
                                        fontWeight: 'bold',
                                     
                                        ...(column.width
                                            ? {
                                                minWidth: column.width,
                                                maxWidth: column.width,
                                                width: column.width,

                                            }
                                            : {
                                                // whiteSpace: 'nowrap',
                                                // overflowX: 'auto',
                                            }),
                                    }}
                                >
                                    {column.label}
                                </TableCell>
                            ))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {displayedRows.map((row, rowIndex) => (
                            <TableRow key={rowIndex} sx={{ backgroundColor: 'white' }}>
                                {showCheckbox && (
                                    <TableCell align="center">
                                        <Checkbox
                                            checked={selectedRows.includes(row.applicationNumber)}
                                            onChange={() => handleCheckboxChange(row.applicationNumber)}
                                        />
                                    </TableCell>
                                )}
                                {columns.map((column) => (
                                    <TableCell
                                        key={column.id}
                                        alignItems="center"

                                        style={{ width: column.width || 'auto' }}
                                        sx={{
                                            border: '1px solid #B2BEB5',
                                            minWidth: column.width,
                                            maxWidth: column.width,
                                            width: column.width,
                                            // overflow: 'hidden',
                                            // textOverflow: 'ellipsis',
                                            // whiteSpace: 'nowrap',
                                        }}                                    >
                                        {column.Cell ? column.Cell({ cell: { getValue: () => row[column.id] } }) : row[column.id]}
                                    </TableCell>
                                ))}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            {/* Pagination */}
            {showPagination && (

                <Box sx={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', marginTop: 2 }}>
                    <Box sx={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', marginBottom: 1 }}>
                        <Typography variant="body1" sx={{ color: 'black', marginRight: 1 }}>
                            Rows Per page:
                        </Typography>
                        <TextField
                            type="number"
                            value={rowsPerPage}
                            onChange={handleRowsPerPageChange}
                            variant="outlined"
                            size="small"
                            sx={{
                                width: '100px', marginRight: 2, backgroundColor: 'white',
                                '& .MuiOutlinedInput-root': {
                                    '& fieldset': { borderColor: primaryColor },
                                    '&:hover fieldset': { borderColor: primaryColor },
                                    '&.Mui-focused fieldset': { borderColor: primaryColor },
                                },
                            }}
                        />
                    </Box>
                    <Button
                        variant="contained"
                        onClick={handlePreviousPage}
                        disabled={currentPage === 1}
                        sx={{
                            marginRight: 1, backgroundColor: primaryColor,
                            textTransform: 'none', color: 'white',
                            '&:hover': { backgroundColor: primaryColor }
                        }}
                    >
                        Previous
                    </Button>
                    <Typography variant="body1" sx={{ marginX: 2 }}>
                        Page {currentPage} of {totalPages}
                    </Typography>
                    <Button
                        variant="contained"
                        onClick={handleNextPage}
                        disabled={currentPage === totalPages}
                        sx={{
                            backgroundColor: primaryColor,
                            textTransform: 'none', color: 'white',
                            '&:hover': { backgroundColor: primaryColor }
                        }}
                    >
                        Next
                    </Button>
                </Box>
            )}
        </Box>
    );
};

export default CustomTable;
