import React, {ReactElement, useEffect, useState} from "react";
import './AddActivity.css'
import {useUser} from "../context/UserContext.tsx";
import {EventActivityData, EventData, UserInEventActivityData} from "../data/data-model.ts";

interface AddActivityFormProps {
    onSubmit: (content: EventActivityData) => void;
    event: EventData;
}

function AddActivity({onSubmit, event}: AddActivityFormProps): ReactElement {
    const {handleChangePage} = useUser();
    const [formData, setFormData] = useState<EventActivityData>({
        name: "",
        description: "",
        datelineDate: new Date(),
        xp: 1,
        event: event
    });
    const [errors, setErrors] = useState<Partial<Record<keyof EventActivityData, string>>>({
        name: "This field is required.",
        description: "This field is required.",
        datelineDate: "This field is required.",
        xp: "This field is required."
    });

    useEffect(() => {
        const xpInput: Element | null = document.querySelector('input[type="number"]');
        const dateInputs: NodeListOf<Element> = document.querySelectorAll('input[type="date"]');
        if (xpInput) {
            xpInput.addEventListener("keydown", function (e: Event) {
                e.preventDefault();
            });
        }
        if (dateInputs) {
            dateInputs.forEach((input: Element) => {
                input.addEventListener("keydown", (e: Event) => e.preventDefault());

            });
        }

        return () => {
            if (xpInput) {
                xpInput.removeEventListener("keydown", function (e: Event) {
                    e.preventDefault();
                });
            }
            if (dateInputs){
                dateInputs.forEach((input: Element) => {
                    input.removeEventListener("keydown", (e: Event) => e.preventDefault());
                });
            }
        };
    }, []);

    function validateField(key: keyof typeof formData, value: string | number | Date): string {
        const alphanumericRegex: RegExp = /^[a-zA-Z0-9 ]+$/;
        switch (key) {
            case "name":
                if (!alphanumericRegex.test(value.toString().trim())) {
                    return "Only alphanumeric characters allowed.";
                }
                return (value.toString().trim().length > 0 && value.toString().trim().length <=30)
                    ? "" : "Name character must be between 1 and 30.";
            case "description":
                if (!alphanumericRegex.test(value.toString().trim())) {
                    return "Only alphanumeric characters allowed.";
                }
                return (value.toString().trim().length > 0 && value.toString().trim().length <=300)
                    ? "" : "Description character must be between 1 and 300.";
            case "datelineDate": {
                if (!(value instanceof Date)) return "Invalid date format.";

                const today: Date = new Date();
                today.setHours(0, 0, 0, 0);
                const dateValue: Date  = new Date(value);
                dateValue.setHours(0, 0, 0, 0);
                const startDate: Date = new Date(event.startDate);
                startDate.setHours(0, 0, 0, 0);

                const endDate: Date | null = event.endDate ? new Date(event.endDate) : null;
                if (endDate) endDate.setHours(0, 0, 0, 0);

                if (dateValue >= startDate && dateValue <= (endDate || dateValue) && dateValue > today) {
                    return "";
                }

                return "Expiration Date must be between the event start and end dates.";
            }
            case "xp":
                if (typeof value === "string"){
                    const intValue: number = parseInt(value.toString());
                    if ( intValue > 0 && intValue <= 80) return "";
                    return "XP must be an integer between 1 and 80.";
                }
                return "XP must be an integer between 1 and 80.";
            default:
                return "This field is required";
        }
    }

    function handleChange(key: keyof typeof formData, value: string | number): void {
        let parsedValue: string | number | Date = value;

        if (key === "datelineDate") {
            parsedValue = new Date(value as string);
        }

        const messageError: string = validateField(key, parsedValue);

        if (messageError !== "") {
            setErrors((prev) => ({
                ...prev,
                [key]: messageError,
            }));
        } else {
            setErrors((prev) => ({ ...prev, [key]: "" }));
        }

        setFormData((prev) => ({ ...prev, [key]: parsedValue }));
    }

    function handleSubmit(e: React.FormEvent): void {
        e.preventDefault();
        let isValid: boolean = true;
        const newErrors: Partial<Record<keyof EventActivityData, string>> = {};

        (Object.keys(formData) as Array<keyof EventActivityData>).forEach((key) => {
            const value: string | number | EventData | Date |
                {[idusereventactivity: number]: UserInEventActivityData; } | undefined = formData[key];
            if (typeof value === "string" || typeof value === "number" || value instanceof Date) {
                const messageError = validateField(key, value);
                if (messageError !== "") {
                    isValid = false;
                    newErrors[key] = messageError;
                }
            }
        });

        setErrors(newErrors);

        if (isValid) {
            onSubmit(formData);
        }
    }

    return (
        <div className="add-activity-container">
            <div className="flex-row">
                <span>Add New Activity</span>
            </div>
            <div className="flex-row">
                <label className="add-event-label" htmlFor="Name">
                    Name
                </label>
                {errors.name && (
                    <img src="/icons/remove.png" className="icon-field-verify" alt="Field Not Verify"/>
                )}
                {!errors.name && (
                    <img src="/icons/tick-mark.png" className="icon-field-verify" alt="Field Verify"/>
                )}
                <input
                    type="text"
                    value={formData.name}
                    onChange={(e) => handleChange("name", e.target.value)}
                    className={errors.name ? "add-event-input input-error" : "add-event-input"}
                />
            </div>
            {errors.name && (
                <div key={"error-name"} className="flex-row">
                    <span className="error">{errors.name}</span>
                </div>
            )}
            <div className="flex-row">
                <label className="add-event-label" htmlFor="Description">
                    Description
                </label>
                {errors.description && (
                    <img src="/icons/remove.png" className="icon-field-verify" alt="Field Not Verify"/>
                )}
                {!errors.description && (
                    <img src="/icons/tick-mark.png" className="icon-field-verify" alt="Field Verify"/>
                )}
                <textarea
                    value={formData.description}
                    onChange={(e) => handleChange("description", e.target.value)}
                    className={errors.description ? "input-error" : ""}
                />
            </div>
            {errors.description && (
                <div key={"error-description"} className="flex-row">
                    <span className="error">{errors.description}</span>
                </div>
            )}
            <div className="flex-row">
                <label className="add-event-label" htmlFor="Expiration Date">Expiration Date</label>
                {errors.datelineDate && (
                    <img src="/icons/remove.png" className="icon-field-verify" alt="Field Not Verify"/>
                )}
                {!errors.datelineDate && (
                    <img src="/icons/tick-mark.png" className="icon-field-verify" alt="Field Verify"/>
                )}
                <input
                    type="date"
                    value={formData.datelineDate ? (formData.datelineDate as Date).toISOString().split("T")[0] : ""}
                    onChange={(e) => handleChange("datelineDate", e.target.value)}
                    className={errors.datelineDate ? "input-error" : ""}
                />
            </div>
            {errors.datelineDate && (
                <div key={"error-expiration-date"} className="flex-row">
                    <span className="error">{errors.datelineDate}</span>
                </div>
            )}
            <div className="flex-row">
                <label className="add-event-label" htmlFor="XP">XP</label>
                {errors.xp && (
                    <img src="/icons/remove.png" className="icon-field-verify" alt="Field Not Verify"/>
                )}
                {!errors.xp && (
                    <img src="/icons/tick-mark.png" className="icon-field-verify" alt="Field Verify"/>
                )}
                <input
                    ref={(el) => el && el.addEventListener("keydown", (e) => e.preventDefault())}
                    type="number"
                    min="1"
                    max="80"
                    step="1"
                    value={formData.xp as number}
                    onChange={(e) => handleChange("xp", e.target.value)}
                    className={errors.xp ? "input-error" : ""}
                />
            </div>
            {errors.xp && (
                <div key={"error-xp"} className="flex-row">
                    <span className="error">{errors.xp}</span>
                </div>
            )}
            <div className="flex-row row-btn-add-event">
                <div className="btn btn-add-event" onClick={() => handleChangePage("Events")}>
                    Cancel
                </div>
                <div className="btn btn-add-event" onClick={handleSubmit}>
                    Add Activity
                </div>
            </div>
        </div>
    );
}

export default AddActivity;
