export class RoleMasterModel {
    public roleCreate?: boolean;
    public roleView?: boolean;
    public roleEdit?: boolean;
    public roleDelete?: boolean;
    public roleGenerate?: boolean;
    public roleName?: string;
}

export class RoleModel {
    public roleName?: string;
    public roleNameMasters?: RoleMasterModel[];
}
