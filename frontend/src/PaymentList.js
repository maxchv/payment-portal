import {List, DateField, Datagrid, TextField, ArrayField} from 'react-admin';

export const PaymentList = (props) => (
    <List {...props}>
        <ArrayField fieldKey="payment_id">
            <Datagrid>
                <TextField source="payment_id" label="Id"/>
                <TextField source="sourceAccount.account_id" label="Source Account Id"/>
                <TextField source="destinationAccount.account_id" label="Destination Account Id"/>
                <TextField source="amount"/>
                <TextField source="reason"/>
                <DateField source="timestamp"/>
                <TextField source="status"/>
            </Datagrid>
        </ArrayField>
    </List>
);