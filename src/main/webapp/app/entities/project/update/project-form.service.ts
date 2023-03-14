import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProject, NewProject } from '../project.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProject for edit and NewProjectFormGroupInput for create.
 */
type ProjectFormGroupInput = IProject | PartialWithRequiredKeyOf<NewProject>;

type ProjectFormDefaults = Pick<NewProject, 'id' | 'isActive'>;

type ProjectFormGroupContent = {
  id: FormControl<IProject['id'] | NewProject['id']>;
  code: FormControl<IProject['code']>;
  name: FormControl<IProject['name']>;
  isActive: FormControl<IProject['isActive']>;
  description: FormControl<IProject['description']>;
  completionDate: FormControl<IProject['completionDate']>;
  regNumber: FormControl<IProject['regNumber']>;
  notes: FormControl<IProject['notes']>;
  address: FormControl<IProject['address']>;
  priority: FormControl<IProject['priority']>;
  progress: FormControl<IProject['progress']>;
  location: FormControl<IProject['location']>;
  exUser: FormControl<IProject['exUser']>;
};

export type ProjectFormGroup = FormGroup<ProjectFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProjectFormService {
  createProjectFormGroup(project: ProjectFormGroupInput = { id: null }): ProjectFormGroup {
    const projectRawValue = {
      ...this.getFormDefaults(),
      ...project,
    };
    return new FormGroup<ProjectFormGroupContent>({
      id: new FormControl(
        { value: projectRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(projectRawValue.code),
      name: new FormControl(projectRawValue.name, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(projectRawValue.isActive),
      description: new FormControl(projectRawValue.description),
      completionDate: new FormControl(projectRawValue.completionDate),
      regNumber: new FormControl(projectRawValue.regNumber),
      notes: new FormControl(projectRawValue.notes),
      address: new FormControl(projectRawValue.address),
      priority: new FormControl(projectRawValue.priority),
      progress: new FormControl(projectRawValue.progress),
      location: new FormControl(projectRawValue.location),
      exUser: new FormControl(projectRawValue.exUser),
    });
  }

  getProject(form: ProjectFormGroup): IProject | NewProject {
    return form.getRawValue() as IProject | NewProject;
  }

  resetForm(form: ProjectFormGroup, project: ProjectFormGroupInput): void {
    const projectRawValue = { ...this.getFormDefaults(), ...project };
    form.reset(
      {
        ...projectRawValue,
        id: { value: projectRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProjectFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
