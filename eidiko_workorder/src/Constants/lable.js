import dayjs from "dayjs";

export const Employee_List_Columns = [
  { id: "actions", label: "Actions" },
  { id: "employeeId", label: "Employee Id" },
  { id: "employeeName", label: "Employee Name" },
  { id: "mashreqReportingManager", label: "Mashreq Reporting Manager" },
  { id: "projectName", label: "Project Name" },
  { id: "projectType", label: "Project Type" },
  {
    id: "contractStartDate",
    label: "Contract Start Date",
    width: 85,
    Cell: ({ cell }) => {
      const value = cell.getValue();
      return <span>{dayjs(value).isValid() ? dayjs(value).format("DD-MMM-YYYY") : ""}</span>;
    },
  },
  {
    id: "startDate",
    label: "Start Date",
    width:85,
    Cell: ({ cell }) => {
      const value = cell.getValue();
      return <span>{dayjs(value).isValid() ? dayjs(value).format("DD-MMM-YYYY") : ""}</span>;
    },
  },
 {
  id: "endDate",
  label: "End Date",
  width: 85,
  Cell: ({ cell }) => {
    const value = cell.getValue();
    const endDate = dayjs(value);
    const today = dayjs();

    if (!endDate.isValid()) return <span></span>;

    let color = 'black';

    // Check if end date is today or in the past → red
    if (endDate.isSame(today, 'day') || endDate.isBefore(today, 'day')) {
      color = '#FF0000';
    }
    // Check if within next 14 days → amber/orange
    else if (endDate.diff(today, 'day') <= 14) {
      color = '#FFBF00'; // amber-like orange
    }

    return (
      <span style={{ color }}>
        {endDate.format("DD-MMM-YYYY")}
      </span>
    );
  },
},
  { id: "sowStatus", label: "SOW Status" },
  { id: "woStatus", label: "WO Status" },
  { id: "poStatus", label: "PO Status" },
  { id: "poWoNumber", label: "WO/PO Number" },
  { id: "comments", label: "Comments" },
  { id: "delete", label: "Delete " }
];

export const updateed_audittrail_columns = [
  { id: "auditAction", label: "Action" },
  { id: "createdUpdatedBy", label: "Created/Updated by" },
  { id: "timestamp", label: "Created/Updated on" },
  { id: "auditUnitTest", label: "Audit Unit Text" },
]