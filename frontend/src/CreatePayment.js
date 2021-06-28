import {
    ArrayInput,
    Create,
    FormDataConsumer,
    NumberInput,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm,
    SimpleFormIterator,
    TextInput
} from 'react-admin';

export const CreatePayment = (props) => (
    <Create {...props}>
        <SimpleForm>
            <ArrayInput source="payments">
                <SimpleFormIterator>
                    <NumberInput source="source_acc_id" label="Source Account Id" min={0} validate={required()}/>
                    <NumberInput source="dest_acc_id" label="Destination Account Id" min={0} validate={required()}/>
                    <NumberInput source="amount" label="Amount" min={0} validate={required()}/>
                    <TextInput source="reason" label="Reason"/>
                </SimpleFormIterator>
            </ArrayInput>
        </SimpleForm>
    </Create>
);