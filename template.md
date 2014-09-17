{%- if apiInfo -%}
#{{apiInfo.title}}

{{apiInfo.description}}

[Terms of Service]({{apiInfo.termsOfServiceUrl}})

Contact: [{{apiInfo.contact}}](mailto:{{apiInfo.contact}})

License: [{{apiInfo.license}}]({{apiInfo.licenseUrl}})

{%- endif -%}

BasePath: {{apiDocuments.0.basePath}}
Api Version: {{apiDocuments.0.apiVersion}}

## APIs
{% for apiDoc in apiDocuments -%}
{% for api in apiDoc.apis -%}
### {{api.path}}

{% if api.description -%}
#### Overview
{{api.description}}
{%- endif %}
{# Operations #}
{%- endfor %}
{%- endfor %}
