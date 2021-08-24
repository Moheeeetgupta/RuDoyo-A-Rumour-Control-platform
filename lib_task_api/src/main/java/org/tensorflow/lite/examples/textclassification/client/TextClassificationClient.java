/*
 * Copyright 2020 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.textclassification.client;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier;

/** Load TfLite model and provide predictions with task api. */
public class TextClassificationClient {
  private static final String TAG = "TaskApi";
  private static final String MODEL_PATH = "text_classification.tflite"; // path to file to map.

  private final Context context;

  /**
   * NLClassifier API classifies input text into different categories.
   * it takes a single string as input, performs classification with string and output pairs as classification result.
   */

  NLClassifier classifier;

  public TextClassificationClient(Context context) {
    this.context = context;
  }

  // tfl model is downloaded first and then
  // load() is called to bring the modelfile into RAM for each input.

  public void load() {
    try {

      // createFromFile creates a memory-mapped file that has specified access mode from a file on disk.

      classifier = NLClassifier.createFromFile(context, MODEL_PATH);
    } catch (IOException e) {
      Log.e(TAG, e.getMessage());
    }
  }

  /**
   * unload() frees RAM and moves modelfile into disk after processing input.
   * The close() method of Reader Class in Java is used to close the stream and release the resources that were busy in the stream
   */

  public void unload() {
    classifier.close();
    classifier = null;
  }

  /**
   * 'classify' classifies input text into different categories(here positive and negative)
   * which is stored in a list.
   * Category is class of tflite where id, label(display name) and confidence(score) are passed as parameter.
   */
  public List<Result> classify(String text) {
    List<Category> apiResults = classifier.classify(text);

    // arraylist of size of above list is intialised to iterate and store list elements.

    List<Result> results = new ArrayList<>(apiResults.size());
    for (int i = 0; i < apiResults.size(); i++) {
      Category category = apiResults.get(i);
      results.add(new Result("" + i, category.getLabel(), category.getScore()));
    }

    /**
     * Collections.sort(list) method sorts list element of Comparable type.
     * After executing above code we'll have the positive and negative probability of input paragraph.
     * Collections.sort(results) sorts the probability; higher value will be displayed first.
     */

    Collections.sort(results);
    return results;
  }
}
