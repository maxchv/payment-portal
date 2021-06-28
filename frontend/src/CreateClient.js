import {ArrayInput, Create, required, SimpleForm, NumberInput, SimpleFormIterator, TextInput} from 'react-admin';

export const CreateClient = (props) => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source="first_name" validate={required()}/>
            <TextInput source="last_name" validate={required()}/>
            <ArrayInput source="accounts">
                <SimpleFormIterator>
                    <TextInput source="account_num" validate={required()} defaultValue={(Math.floor(1000000 * Math.random()) + 1).toString()}/>
                    <TextInput source="account_type" validate={required()} defaultValue={"card/simple"}/>
                    <NumberInput source="balance" validate={required()} min={0}/>
                </SimpleFormIterator>
            </ArrayInput>
        </SimpleForm>
    </Create>
);