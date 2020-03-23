/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.buck.skylark.parser.context;

import com.facebook.buck.core.path.ForwardRelativePath;
import com.facebook.buck.core.util.immutables.BuckStyleValue;
import com.facebook.buck.util.collect.TwoArraysImmutableHashMap;
import com.google.common.base.Preconditions;

/** Rule representation internal to Starlark parser. */
@BuckStyleValue
public abstract class RecordedRule {
  public abstract ForwardRelativePath getBasePath();

  public abstract String getBuckType();

  public abstract TwoArraysImmutableHashMap<String, Object> getRawRule();

  public static RecordedRule of(
      ForwardRelativePath basePath,
      String buckType,
      TwoArraysImmutableHashMap<String, Object> args) {
    Preconditions.checkArgument(!buckType.isEmpty());
    return ImmutableRecordedRule.ofImpl(basePath, buckType, args);
  }
}