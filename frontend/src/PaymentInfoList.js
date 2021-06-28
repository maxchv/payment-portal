import {ArrayField, Datagrid, DateField, Filter, List, NumberInput, TextField} from 'react-admin';
import {Typography} from '@material-ui/core';


const PaymentFilter = (props) => {
    return (
        <Filter {...props}>
            <NumberInput label="Payer Id" source="payer_id" min={1} alwaysOn/>
            <NumberInput label="Recipient Id" source="recipient_id" min={1} alwaysOn/>
            <NumberInput label="Source Account Id" source="source_acc_id" min={1} alwaysOn/>
            <NumberInput label="Destination Account Id" source="dest_acc_id" min={1} alwaysOn/>
        </Filter>
    )
};

const Aside = () =>
    <div style={{width: 200, margin: '1em'}}>
        <Typography variant="h6">Filter payments</Typography>
        <Typography variant="body2">
            To see payments put your query to the filters:
            <ul>
                <li>Payment Id</li>
                <li>Recipient Id</li>
                <li>Source Account Id</li>
                <li>Destination Account Id</li>
            </ul>
        </Typography>
    </div>
;

export const PaymentInfoList = (props) => (
<List {...props} filters={<PaymentFilter/>} aside={<Aside/>}>
    <ArrayField fieldKey="payment_id">
        <Datagrid>
            <TextField source="payment_id" label="Id"/>
            <DateField source="timestamp"/>
            <TextField source="src_acc_num" label="Source Account"/>
            <TextField source="dest_acc_num" label="Destination Account"/>
            <TextField source="amount"/>
            <TextField source="reason"/>
            <TextField source="status"/>
            <TextField source="payer.first_name"/>
            <TextField source="payer.last_name"/>
            <TextField source="recipient.first_name"/>
            <TextField source="recipient.last_name"/>
        </Datagrid>
    </ArrayField>
</List>
);