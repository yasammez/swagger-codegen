package com.wordnik.codegen.resource;

import com.wordnik.codegen.FieldDefinition;
import com.wordnik.codegen.config.DataTypeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: ramesh
 * Date: 3/31/11
 * Time: 7:57 AM
 */
public class ModelField {
	
    private String name;

    private String wrapperName;
    
    private String description = "";

    private String defaultValue;

    private boolean required = false;

    private List<String> allowableValues = null;

    private String paramType;

    private String dataType;

    private String internalDescription;

    private String paramAccess;

    private FieldDefinition fieldDefinition;
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	public String getWrapperName() {
		return wrapperName;
	}

	public void setWrapperName(String wrapperName) {
		this.wrapperName = wrapperName;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public List<String> getAllowableValues() {
		return allowableValues;
	}

	public void setAllowableValues(List<String> allowableValues) {
		this.allowableValues = allowableValues;
	}

    public String getAllowedValuesString() {
        String result = "";
        for(String allowedValue: this.allowableValues){
            result += (allowedValue +",");
        }

        return result.substring(0, result.length());
    }

    public void setAllowedValues(String csvAlowedValue) {
        List<String> allowedValues = new ArrayList<String>();
        if (csvAlowedValue != null) {
            StringTokenizer tokenizer = new StringTokenizer( csvAlowedValue, "," );
            while(tokenizer.hasMoreTokens()){
                tokenizer.nextToken(",");
            }
        }
        this.setAllowableValues(allowedValues);
    }

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getInternalDescription() {
		return internalDescription;
	}

	public void setInternalDescription(String internalDescription) {
		this.internalDescription = internalDescription;
	}

	public String getParamAccess() {
		return paramAccess;
	}

	public void setParamAccess(String paramAccess) {
		this.paramAccess = paramAccess;
	}

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public FieldDefinition getFieldDefinition(){
        return fieldDefinition;
    }

    public FieldDefinition getFieldDefinition(DataTypeMapper dataTypeMapper) {
    	if(fieldDefinition == null) {
    		fieldDefinition = new FieldDefinition();
	    	String type = paramType.trim();
	    	if(type.contains("date")||type.contains("Date") ){
	    		fieldDefinition.getImportDefinitions().add("java.util.Date");
	    	}
	    	if(type.startsWith("List[")){
	    		fieldDefinition.getImportDefinitions().addAll(dataTypeMapper.getListImportPackages());
	    		String entryType = type.substring(5, type.length()-1);
	    		entryType =  dataTypeMapper.getObjectType(entryType, true);
	    		String returnType = dataTypeMapper.getListReturnTypeSignature(entryType);
	    		fieldDefinition.setReturnType(returnType);
	    		fieldDefinition.setInitialization(" = " + dataTypeMapper.generateListInitialization(entryType));
	    		if(this.getWrapperName() != null){
	    			fieldDefinition.setName(this.getWrapperName());
	    		}else{
	    			fieldDefinition.setName(this.getName());
	    		}
	    		
	    	}else if (type.startsWith("Map[")) {
                fieldDefinition.getImportDefinitions().addAll(dataTypeMapper.getMapImportPackages());
                String keyClass, entryClass = "";
	    		String entryType = type.substring(4, type.length()-1);
                keyClass = entryType.substring(0, entryType.indexOf(",") );
                entryClass = entryType.substring(entryType.indexOf(",") + 1, entryType.length());
	    		//entryType =  dataTypeMapper.getObjectType(entryType, true);
	    		entryType =  dataTypeMapper.getObjectType(keyClass, true) + "," + dataTypeMapper.getObjectType(entryClass, true);
	    		String returnType = dataTypeMapper.getMapReturnTypeSignature(entryType);
	    		fieldDefinition.setReturnType(returnType);
	    		fieldDefinition.setInitialization("= " + dataTypeMapper.generateMapInitialization(entryType));
	    		if(this.getWrapperName() != null){
	    			fieldDefinition.setName(this.getWrapperName());
	    		}else{
	    			fieldDefinition.setName(this.getName());
	    		}
	    	}else{
	    		fieldDefinition.setReturnType(dataTypeMapper.getObjectType(type, false));
	    		fieldDefinition.setName(this.getName());
	    	}
	    	
    	}
    	return fieldDefinition;
    }
    
}
