export interface ReportGeneration {
    id?: number;
    relationshipManager?: any;
    distributorMaster?: any;
    reportType?: string;
    vchReportGeneratedBy?: string;
    dtReportGeneratedDate?: string;
    startDate?: Date;
    toDate?: Date;
    aifCalculation?: string;
}
