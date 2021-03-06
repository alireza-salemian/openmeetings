/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openmeetings.web.admin;

import org.apache.openmeetings.web.app.Application;
import org.apache.openmeetings.web.common.ConfirmableAjaxBorder;
import org.apache.openmeetings.web.common.FormSaveRefreshPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

public abstract class AdminSavePanel<T> extends FormSaveRefreshPanel<T> {
	private static final long serialVersionUID = 1L;
	private final Label newRecord;
	private final Form<T> form;

	public AdminSavePanel(String id, final Form<T> form) {
		super(id, form);
		this.form = form;

		newRecord = new Label("newRecord", Model.of(Application.getString("155")));
		add(newRecord.setVisible(false).setOutputMarkupId(true));
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		final AjaxButton newBtn = new AjaxButton("ajax-new-button", form) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				// repaint the feedback panel so that it is hidden
				target.add(feedback);
				newRecord.setVisible(true);
				target.add(newRecord);
				onNewSubmit(target, form);
			}

			@Override
			protected void onError(AjaxRequestTarget target) {
				// repaint the feedback panel so errors are shown
				target.add(feedback);
				onNewError(target, form);
			}
		};
		// add a cancel button that can be used to submit the form via ajax
		final ConfirmableAjaxBorder delBtn = new ConfirmableAjaxBorder("ajax-cancel-button", getString("80"), getString("833"), (Form<?>)null) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target) {
				// repaint the feedback panel so errors are shown
				target.add(feedback);
				hideNewRecord();
				onDeleteError(target, form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				// repaint the feedback panel so that it is hidden
				target.add(feedback);
				hideNewRecord();
				onDeleteSubmit(target, form);
			}
		};
		add(newBtn.setVisible(isNewBtnVisible()), delBtn.setVisible(isDelBtnVisible()));
	}

	/**
	 * Hide the new record text
	 */
	@Override
	public void hideNewRecord() {
		newRecord.setVisible(false);
	}

	/**
	 * Hide the new record text
	 */
	public void showNewRecord() {
		newRecord.setVisible(true);
	}

	protected abstract boolean isNewBtnVisible();
	protected abstract boolean isDelBtnVisible();

	protected abstract void onNewSubmit(AjaxRequestTarget target, Form<?> form);
	protected abstract void onNewError(AjaxRequestTarget target, Form<?> form);

	protected abstract void onDeleteSubmit(AjaxRequestTarget target, Form<?> form);
	protected abstract void onDeleteError(AjaxRequestTarget target, Form<?> form);
}
