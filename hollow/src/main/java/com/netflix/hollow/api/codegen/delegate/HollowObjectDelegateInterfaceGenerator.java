/*
 *
 *  Copyright 2016 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.hollow.api.codegen.delegate;

import static com.netflix.hollow.api.codegen.HollowCodeGenerationUtils.delegateInterfaceName;
import static com.netflix.hollow.api.codegen.HollowCodeGenerationUtils.substituteInvalidChars;
import static com.netflix.hollow.api.codegen.HollowCodeGenerationUtils.typeAPIClassname;
import static com.netflix.hollow.api.codegen.HollowCodeGenerationUtils.uppercase;

import com.netflix.hollow.api.custom.HollowAPI;

import com.netflix.hollow.core.schema.HollowObjectSchema;
import com.netflix.hollow.api.codegen.HollowAPIGenerator;
import com.netflix.hollow.api.codegen.HollowJavaFileGenerator;
import com.netflix.hollow.api.objects.delegate.HollowObjectDelegate;

/**
 * This class contains template logic for generating a {@link HollowAPI} implementation.  Not intended for external consumption.
 * 
 * @see HollowAPIGenerator
 * 
 * @author dkoszewnik
 *
 */
public class HollowObjectDelegateInterfaceGenerator implements HollowJavaFileGenerator {

    private final String packageName;
    private final HollowObjectSchema schema;
    private final String className;

    public HollowObjectDelegateInterfaceGenerator(String packageName, HollowObjectSchema schema) {
        this.packageName = packageName;
        this.schema = schema;
        this.className = delegateInterfaceName(schema.getName());
    }


    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String generate() {
        StringBuilder classBuilder = new StringBuilder();

        classBuilder.append("package ").append(packageName).append(";\n\n");

        classBuilder.append("import ").append(HollowObjectDelegate.class.getName()).append(";\n\n");

        classBuilder.append("\n@SuppressWarnings(\"all\")\n");
        classBuilder.append("public interface ").append(className).append(" extends HollowObjectDelegate {\n\n");

        for(int i=0;i<schema.numFields();i++) {
            String methodFieldName = substituteInvalidChars(uppercase(schema.getFieldName(i)));
            switch(schema.getFieldType(i)) {
            case BOOLEAN:
                classBuilder.append("    public boolean get").append(methodFieldName).append("(int ordinal);\n\n");
                classBuilder.append("    public Boolean get").append(methodFieldName).append("Boxed(int ordinal);\n\n");
                break;
            case BYTES:
                classBuilder.append("    public byte[] get").append(methodFieldName).append("(int ordinal);\n\n");
                break;
            case DOUBLE:
                classBuilder.append("    public double get").append(methodFieldName).append("(int ordinal);\n\n");
                classBuilder.append("    public Double get").append(methodFieldName).append("Boxed(int ordinal);\n\n");
                break;
            case FLOAT:
                classBuilder.append("    public float get").append(methodFieldName).append("(int ordinal);\n\n");
                classBuilder.append("    public Float get").append(methodFieldName).append("Boxed(int ordinal);\n\n");
                break;
            case INT:
                classBuilder.append("    public int get").append(methodFieldName).append("(int ordinal);\n\n");
                classBuilder.append("    public Integer get").append(methodFieldName).append("Boxed(int ordinal);\n\n");
                break;
            case LONG:
                classBuilder.append("    public long get").append(methodFieldName).append("(int ordinal);\n\n");
                classBuilder.append("    public Long get").append(methodFieldName).append("Boxed(int ordinal);\n\n");
                break;
            case REFERENCE:
                classBuilder.append("    public int get").append(methodFieldName).append("Ordinal(int ordinal);\n\n");
                break;
            case STRING:
                classBuilder.append("    public String get").append(methodFieldName).append("(int ordinal);\n\n");
                classBuilder.append("    public boolean is").append(methodFieldName).append("Equal(int ordinal, String testValue);\n\n");
                break;
            }
        }

        classBuilder.append("    public ").append(typeAPIClassname(schema.getName())).append(" getTypeAPI();\n\n");

        classBuilder.append("}");

        return classBuilder.toString();
    }

}
