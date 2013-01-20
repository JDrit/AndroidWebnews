/**
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Uless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/	
package edu.rit.csh.androidwebnews;

public interface ActivityInterface {
	
	/**
	 * Used to make sure that the Activities have a method that can be called
	 * when the async task finishes and needs to update the Activity / Fragment.
	 * This should then call the update method in the frgment
	 * @param jsonString - the output of the async task
	 */
	public void update(String jsonString);
	
	public void onNewsgroupSelected(final String newsgroupName);
}