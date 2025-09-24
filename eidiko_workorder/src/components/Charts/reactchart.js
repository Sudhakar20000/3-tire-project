import { PieChart } from '@mui/x-charts';
import React from 'react';
import { Box, Typography, Stack } from '@mui/material';

const COLORS = ['#00bcd4', '#448aff', '#4caf50', '#ff9800'];

export default function BasicPie({ chartData }) {
  return (
    <Box display="flex" alignItems="center" gap={1}>
      <PieChart
        series={[
          {
            data: chartData,
            label: { visible: false },
            arcLabel: (item) => `${item.value}`, 
            arcLabelMinAngle: 10, 
          },
        ]}
        width={300}
        height={150}
        legend={{ hidden: true }}
      />
      <Stack spacing={1}>
        {chartData.map((item, index) => (
          <Box key={item.id} display="flex" alignItems="center" gap={1}>
            <Box
              sx={{
                width: 14,
                height: 14,
                borderRadius: '50%',
                backgroundColor: COLORS[index % COLORS.length],
              }}
            />
            <Typography variant="body2">
              {item.label}: {item.value}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Box>
  );
}
