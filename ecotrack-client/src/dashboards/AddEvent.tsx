import React, {ReactElement, useEffect, useState} from "react";
import './AddEvent.css'
import {useUser} from "../context/UserContext.tsx";
import {EventActivityData, EventData, GroupData, UserInEventData} from "../data/data-model.ts";
import {useError} from "../context/ErrorContext.tsx";

interface AddEventFormProps {
    onSubmit: (content: EventData) => void;
    organizer: GroupData;
}

function AddEvent({onSubmit, organizer}: AddEventFormProps): ReactElement {
    const {handleChangePage} = useUser();
    const {addMessage} = useError();
    const [formData, setFormData] = useState<EventData>({
        name: "",
        description: "",
        startDate: new Date(),
        endDate: new Date(),
        xp: 5,
        category: "",
        place: "",
        organizer: organizer
    });
    const [errors, setErrors] = useState<Partial<Record<keyof EventData, string>>>({
        name: "This field is required.",
        description: "This field is required.",
        startDate: "This field is required.",
        endDate: "This field is required.",
        xp: "This field is required.",
        category: "This field is required.",
        place: "This field is required.",
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
            case "place":
                if (!alphanumericRegex.test(value.toString().trim())) {
                    return "Only alphanumeric characters allowed.";
                }
                return (value.toString().trim().length > 0 && value.toString().trim().length <=100)
                    ? "" : "Place character must be between 1 and 100.";
            case "description":
                if (!alphanumericRegex.test(value.toString().trim())) {
                    return "Only alphanumeric characters allowed.";
                }
                return (value.toString().trim().length > 0 && value.toString().trim().length <=300)
                    ? "" : "Description character must be between 1 and 300.";
            case "startDate": {
                const today: Date = new Date();
                today.setHours(0, 0, 0, 0);
                if ((value as Date) > today) return "";
                return "Start Date must be after today.";
            }
            case "endDate": {
                if ((value as Date) > formData.startDate) return "";
                return "End Date must be after Start Date.";
            }
            case "xp":
                if (typeof value === "string") {
                    const intValue: number = parseInt(value.toString());
                    if ( intValue > 4 && intValue <= 150) return "";
                    return "XP must be an integer between 5 and 150.";
                }
                return "XP must be an integer between 5 and 150.";
            case "category":
                if ([
                    "Beach Cleaning",
                    "Environmental Education",
                    "Reforestation",
                    "River Restoration",
                    "Sustainable Energy",
                    "Waste Sorting",
                    "Wildlife Conservation"
                ].includes(value.toString())) return "";
                return "Category not valid.";
            default:
                return "This field is required";
        }
    }

    function handleChange(key: keyof typeof formData, value: string | number): void {
        if (key === "organizer") return;
        let parsedValue: string | number | Date = value;
        if (key === "startDate" || key === "endDate") {
            parsedValue = new Date(value as string);
        }
        const messageError: string = validateField(key, parsedValue);
        setErrors((prev) => ({ ...prev, [key]: messageError }));
        setFormData((prev) => ({ ...prev, [key]: parsedValue }));
    }

    function handleSubmit(e: React.FormEvent): void {
        e.preventDefault();
        let isValid: boolean = true;
        const newErrors: Partial<Record<keyof EventData, string>> = {};

        (Object.keys(formData) as Array<keyof EventData>).forEach((key) => {
            const value: string | number | boolean | GroupData | Date |
                {[p: number]: UserInEventData } |
                {[p: number]: EventActivityData } | undefined = formData[key];
            if (typeof value === "string" || typeof value === "number" || value instanceof Date) {
                const messageError: string = validateField(key, value);
                if (messageError !== "") {
                    isValid = false;
                    newErrors[key] = messageError;
                }
            }
        });

        setErrors(newErrors);

        if (isValid) {
            addMessage("Attendere prego", false)
            onSubmit(formData);
        }
    }

    return (
        <div className="add-event-container">
            <div className="flex-row">
                <span>Add New Event</span>
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
                <label className="add-event-label" htmlFor="Start Date">Start Date</label>
                {errors.startDate && (
                    <img src="/icons/remove.png" className="icon-field-verify" alt="Field Not Verify"/>
                )}
                {!errors.startDate && (
                    <img src="/icons/tick-mark.png" className="icon-field-verify" alt="Field Verify"/>
                )}
                <input
                    type="date"
                    value={formData.startDate ? (formData.startDate as Date).toISOString().split("T")[0] : ""}
                    onChange={(e) => handleChange("startDate", e.target.value)}
                    className={errors.startDate ? "input-error" : ""}
                />

            </div>
            {errors.startDate && (
                <div key={"error-start-date"} className="flex-row">
                    <span className="error">{errors.startDate}</span>
                </div>
            )}
            <div className="flex-row">
                <label className="add-event-label" htmlFor="End Date">End Date</label>
                {errors.endDate && (
                    <img src="/icons/remove.png" className="icon-field-verify" alt="Field Not Verify"/>
                )}
                {!errors.endDate && (
                    <img src="/icons/tick-mark.png" className="icon-field-verify" alt="Field Verify"/>
                )}
                <input
                    type="date"
                    value={formData.endDate ? (formData.endDate as Date).toISOString().split("T")[0] : ""}
                    onChange={(e) => handleChange("endDate", e.target.value)}
                    className={errors.endDate ? "input-error" : ""}
                />
            </div>
            {errors.endDate && (
                <div key={"error-end-date"} className="flex-row">
                    <span className="error">{errors.endDate}</span>
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
                    step="1"
                    min="5"
                    max="150"
                    value={formData.xp as number}
                    onChange={(e) => handleChange("xp", e.target.value)}
                    className={errors.endDate ? "input-error" : ""}
                />
            </div>
            {errors.xp && (
                <div key={"error-xp"} className="flex-row">
                    <span className="error">{errors.xp}</span>
                </div>
            )}
            <div className="flex-row">
                <label className="add-event-label" htmlFor="Category">Category</label>
                {errors.category && (
                    <img src="/icons/remove.png" className="icon-field-verify" alt="Field Not Verify"/>
                )}
                {!errors.category && (
                    <img src="/icons/tick-mark.png" className="icon-field-verify" alt="Field Verify"/>
                )}
                <select
                    className={errors.category ? "add-event-input input-error" : "add-event-input"}
                    value={formData.category}
                    onChange={(e) => handleChange("category", e.target.value)}>
                    <option value="">Select a category</option>
                    <option value="Beach Cleaning">Beach Cleaning</option>
                    <option value="Environmental Education">Environmental Education</option>
                    <option value="Reforestation">Reforestation</option>
                    <option value="River Restoration">River Restoration</option>
                    <option value="Sustainable Energy">Sustainable Energy</option>
                    <option value="Waste Sorting">Waste Sorting</option>
                    <option value="Wildlife Conservation">Wildlife Conservation</option>
                </select>
            </div>
            {errors.category && (
                <div key={"error-category"} className="flex-row">
                    <span className="error">{errors.category}</span>
                </div>
            )}
            <div className="flex-row">
                <label className="add-event-label" htmlFor="Place">Place</label>
                {errors.place && (
                    <img src="/icons/remove.png" className="icon-field-verify" alt="Field Not Verify"/>
                )}
                {!errors.place && (
                    <img src="/icons/tick-mark.png" className="icon-field-verify" alt="Field Verify"/>
                )}
                <input
                    className={errors.place ? "add-event-input input-error" : "add-event-input"}
                    type="text"
                    value={formData.place}
                    onChange={(e) => handleChange("place", e.target.value)}
                />
            </div>
            {errors.place && (
                <div key={"error-place"} className="flex-row">
                    <span className="error">{errors.place}</span>
                </div>
            )}
            <div className="flex-row row-btn-add-event">
                <div className="btn btn-add-event" onClick={() => handleChangePage("Group")}>
                    Cancel
                </div>
                <div className="btn btn-add-event" onClick={handleSubmit}>
                    Add Event
                </div>
            </div>
        </div>
    );
}

export default AddEvent;
