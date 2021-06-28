import {List, Filter, NumberInput, Datagrid, TextField, ArrayField} from 'react-admin';

const AccountFilter = (props) => (
    <Filter {...props}>
        <NumberInput min={1} label="Client Id" source="client_id" alwaysOn />
    </Filter>
);

export const AccountList = (props) => (
  <List {...props} filters={<AccountFilter/>}>
      <ArrayField source="accounts" fieldKey="account_id">
          <Datagrid>
              <TextField source="account_id" label="Id"/>
              <TextField source="account_num"/>
              <TextField source="account_type"/>
              <TextField source="balance"/>
          </Datagrid>
      </ArrayField>
  </List>
);

