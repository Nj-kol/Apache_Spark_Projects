
# Schema Evolution with Confluent Schema Registry

## Steps


## Evolve schema

**v1**

```json
{
     "type": "record",
     "namespace": "com.njkol.model",
     "name": "Customer",
     "version": "1",
     "fields": [
       { "name": "first_name", "type": "string", "doc": "First Name of Customer" },
       { "name": "last_name", "type": "string", "doc": "Last Name of Customer" },
       { "name": "age", "type": "int", "doc": "Age at the time of registration" },
       { "name": "height", "type": "float", "doc": "Height at the time of registration in cm" },
       { "name": "weight", "type": "float", "doc": "Weight at the time of registration in kg" },
       { "name": "automated_email", "type": "boolean", "default": true, "doc": "Field indicating if the user is enrolled in marketing emails" }
     ]
}
```

**v2**

```json
{
     "type": "record",
     "namespace": "com.njkol.model",
     "name": "Customer",
     "version": "2",
     "fields": [
       { "name": "first_name", "type": "string", "doc": "First Name of Customer" },
       { "name": "last_name", "type": "string", "doc": "Last Name of Customer" },
       { "name": "age", "type": "int", "doc": "Age at the time of registration" },
       { "name": "height", "type": "float", "doc": "Height at the time of registration in cm" },
       { "name": "weight", "type": "float", "doc": "Weight at the time of registration in kg" },
       { "name": "phone_number", "type": ["null", "string"], "default": null, "doc": "optional phone number"},
       { "name": "email", "type": "string", "default": "missing@example.com", "doc": "email address"}
     ]
}
```

We have evolved the schema by :

* Removing the field `automated_email`
* Adding two new fields `phone_number` and email

