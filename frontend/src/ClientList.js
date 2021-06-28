import {ChipField, Datagrid, List, TextField} from 'react-admin';


export const ClientList = props => (
    <List {...props}>
        <Datagrid rowClick="edit">
            <TextField source="id"/>
            <TextField source="first_name"/>
            <TextField source="last_name"/>
            <ChipField source="accounts.length" label="Accounts"/>
        </Datagrid>
    </List>
);