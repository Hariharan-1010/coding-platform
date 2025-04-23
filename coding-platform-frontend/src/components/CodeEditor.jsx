import { useState } from "react";
import AceEditor from "react-ace";
import axios from "axios";

import "ace-builds/src-noconflict/mode-java";
import "ace-builds/src-noconflict/mode-python";
import "ace-builds/src-noconflict/theme-monokai";
import { useNavigate } from "react-router-dom";

function CodeEditor() {
  const [code, setCode] = useState("");
  const [language, setLanguage] = useState("java");
  const [stdin, setStdin] = useState("");
  const [output, setOutput] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async () => {
    try {
      const token = localStorage.getItem("token");
      const processedStdin = stdin.replace(/\\n/g, "\n");
      const response = await axios.post(
        "http://localhost:8080/coding-platform-backend-1.0-SNAPSHOT/api/execute",
        {
          sourceCode: code,
          languageId: language === "java" ? 62 : 71,
          stdin: processedStdin,
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      console.log(response.data)
      setOutput(response.data);
    } catch (err) {
      console.error("Submission error:", err);
      alert("Submission failed: " + err.message);
    }
  };

  const handleLogout = async () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <div className="min-h-screen bg-gray-100 p-4">
      <button
        className="bg-red-500 text-white px-4 py-2 rounded-md cursor-pointer"
        onClick={handleLogout}
      >
        Logout
      </button>
      <div className="max-w-4xl mx-auto">
        <h2 className="text-2xl font-bold mb-4">Code Editor</h2>
        <select
          value={language}
          onChange={(e) => setLanguage(e.target.value)}
          className="mb-4 p-2 border rounded"
        >
          <option value="java">Java</option>
          <option value="python">Python</option>
        </select>
        <AceEditor
          mode={language}
          theme="monokai"
          onChange={setCode}
          value={code}
          width="100%"
          height="500px"
          setOptions={{
            enableBasicAutocompletion: true,
            enableLiveAutocomplete: true,
            enableSnippets: true,
            fontSize: 14,
            showPrintMargin: false,
          }}
        />
        <div className="mt-5 items-center gap-4 flex flex-col">
          <label htmlFor="stdin" className="font-semibold">
            Stdin
          </label>
          <textarea
            id="stdin"
            value={stdin}
            onChange={(e) => setStdin(e.target.value)}
            className="border border-blue-950 outline-none w-full px-4 py-2 rounded resize-y"
            rows="4"
            placeholder="Enter input (e.g., 3\n5 for two lines)"
          />
        </div>
        <button
          onClick={handleSubmit}
          className="mt-4 bg-blue-500 text-white p-2 rounded hover:bg-blue-600"
        >
          Submit Code
        </button>
        {output?.compile_output && (
          <p className="mt-4 border-2 border-slate-500 font-mono flex text-red-500 bg-slate-100">
            <span className="bg-red-500 py-4 px-8 text-white">Compile Output:</span>
            <span className="py-4 px-8">{output.compile_output}</span>
          </p>
        )}
        {output?.stderr && (
          <p className="mt-4 border-2 border-slate-500 font-mono flex text-red-500 bg-slate-100">
            <span className="bg-red-500 py-4 px-8 text-white">Error:</span>
            <span className="py-4 px-8">{output.stderr}</span>
          </p>
        )}
        {output?.stdout && (
          <p className="mt-4 border-2 flex border-slate-500 font-mono bg-slate-100 text-emerald-400">
            <span className="bg-emerald-500 py-4 px-8 text-white">Output:</span>
            <span className="py-4 px-8">{output.stdout}</span>
          </p>
        )}
      </div>
    </div>
  );
}

export default CodeEditor;